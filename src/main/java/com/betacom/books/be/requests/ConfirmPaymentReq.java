package com.betacom.books.be.requests;

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
public class ConfirmPaymentReq {
    private String paymentIntentId;
    private Integer orderId;
}