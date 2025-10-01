package com.betacom.books.be.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaymentDTO {
    private String id;
    private Integer orderId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String paymentIntentId;
    private String clientSecret;
    private LocalDateTime createdAt;
    private String errorMessage;
}