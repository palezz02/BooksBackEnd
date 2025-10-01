package com.betacom.books.be.requests;

import java.math.BigDecimal;
import java.util.List;

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
public class PaymentReq {
    private BigDecimal amount;
    private String currency;
    private Integer orderId;
    private List<Integer> orderItemIds;
    private String customerEmail;
    private String description;
}
