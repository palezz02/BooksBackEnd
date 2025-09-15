package com.betacom.books.be.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.models.OrderItem;

public class Utilities {
	public List<OrderItemDTO> buildOrderItems(List<OrderItem> orderItems){
		return orderItems.stream()
				.map(orderItem -> OrderItemDTO.builder()
						.id(orderItem.getId())
						.order(orderItem.getOrder())
						.inventory(orderItem.getInventory())
						.quantity(orderItem.getQuantity())
						.unitPrice(orderItem.getUnitPrice())
						.subtotal(orderItem.getSubtotal())
						.build())
				.collect(Collectors.toList());
	}
	
}
