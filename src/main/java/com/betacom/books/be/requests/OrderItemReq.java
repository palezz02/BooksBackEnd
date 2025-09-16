package com.betacom.books.be.requests;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Order;

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
