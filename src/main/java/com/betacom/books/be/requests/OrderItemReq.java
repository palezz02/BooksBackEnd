package com.betacom.books.be.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemReq {
	private Integer id;
	private Integer orderId;
	private Integer inventoryId;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotal;
}
