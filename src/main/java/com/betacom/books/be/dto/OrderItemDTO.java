package com.betacom.books.be.dto;

import java.math.BigDecimal;

import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderItemDTO {
	private Long id;
	private Order order;
	private Inventory inventory;
	private Integer quantity;
	private BigDecimal unitPrice;
	private BigDecimal subtotal;
}
