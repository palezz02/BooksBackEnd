package com.betacom.books.be.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class CreatePaymentIntentReq {
    private BigDecimal amount;
    private String currency;
    private String customerEmail;
    private String description;
    private Integer orderId;
}