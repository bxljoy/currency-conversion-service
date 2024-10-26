package com.alex.microservices.currency_conversion_service.controllers;

import org.springframework.web.bind.annotation.RestController;
import com.alex.microservices.currency_conversion_service.domain.CurrencyConversion;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class CurrencyConversionController {

    @GetMapping("/currency-conversion/from/{from}/to/{to}/quantity/{quantity}")
    public ResponseEntity<CurrencyConversion> calculateCurrencyConversion(@PathVariable String from,
            @PathVariable String to,
            @PathVariable Long quantity) {
        return null;
    }

}
