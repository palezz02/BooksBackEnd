package com.betacom.books.be.requests;

import java.math.BigDecimal;

import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Order;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderItemReq {
	private Long id;
	private Order order;
	private Inventory inventory;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotal;
}
