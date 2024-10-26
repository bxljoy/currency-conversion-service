package com.alex.microservices.currency_conversion_service.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alex.microservices.currency_conversion_service.domain.CurrencyConversion;
import com.alex.microservices.currency_conversion_service.services.CurrencyExchangeProxy;

import java.math.BigDecimal;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CurrencyConversionController {

    private final CurrencyExchangeProxy proxy;

    public CurrencyConversionController(CurrencyExchangeProxy proxy) {
        this.proxy = proxy;
    }

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> calculateCurrencyConversion(@PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {
        HashMap<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);

        ResponseEntity<CurrencyConversion> responseEntity = new RestTemplate().getForEntity(
                "http://localhost:8000/currency-exchange/from/{from}/to/{to}",
                CurrencyConversion.class, uriVariables);

        CurrencyConversion currencyConversion = responseEntity.getBody();
        if (currencyConversion != null) {
            // currencyConversion.setEnvironment(port);
            currencyConversion.setQuantity(quantity);
            currencyConversion.setTotalCalculateAmount(
                    quantity.multiply(currencyConversion.getConversionMultiple()));
            return new ResponseEntity<>(currencyConversion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    @GetMapping("/currency-conversion-feign/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> calculateCurrencyConversionFeign(@PathVariable String from,
            @PathVariable String to,
            @PathVariable BigDecimal quantity) {

        CurrencyConversion currencyConversion = proxy.retrieveExchangeValue(from, to);

        if (currencyConversion != null) {
            currencyConversion.setQuantity(quantity);
            currencyConversion.setEnvironment(currencyConversion.getEnvironment() + " feign");
            currencyConversion.setTotalCalculateAmount(
                    quantity.multiply(currencyConversion.getConversionMultiple()));
            return new ResponseEntity<>(currencyConversion, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

}
