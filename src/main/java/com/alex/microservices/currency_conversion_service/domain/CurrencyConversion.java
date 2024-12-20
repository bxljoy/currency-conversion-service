package com.alex.microservices.currency_conversion_service.domain;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CurrencyConversion {

    private Long id;

    private String from;

    private String to;

    private BigDecimal conversionMultiple;

    private BigDecimal quantity;

    private BigDecimal totalCalculateAmount;

    private String environment;
}
