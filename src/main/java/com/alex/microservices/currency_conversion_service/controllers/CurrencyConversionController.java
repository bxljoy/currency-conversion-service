package com.alex.microservices.currency_conversion_service.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alex.microservices.currency_conversion_service.domain.CurrencyConversion;
import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CurrencyConversionController {

    private final Environment environment;

    public CurrencyConversionController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> calculateCurrencyConversion(@PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {
        String port = environment.getProperty("local.server.port");
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);

        CurrencyConversion currencyConversion = responseEntity.getBody();
        if (currencyConversion != null) {
            currencyConversion.setEnvironment(port);
            currencyConversion.setQuantity(quantity);
            currencyConversion.setTotalCalculateAmount(
                    quantity.multiply(currencyConversion.getConversionMultiple()));
            return new ResponseEntity<>(currencyConversion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
