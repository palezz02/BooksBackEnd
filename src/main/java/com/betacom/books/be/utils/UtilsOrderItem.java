package com.betacom.books.be.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.models.OrderItem;

public class UtilsOrderItem {
	public List<OrderItemDTO> buildOrderItemListDTO(List<OrderItem> lista){
		return lista.stream().map(o -> OrderItemDTO.builder()
				.id(o.getId())
				.order(o.getOrder())
				.inventory(o.getInventory())
				.quantity(o.getQuantity())
				.unitPrice(o.getUnitPrice())
				.subtotal(o.getSubtotal())
				.build()).collect(Collectors.toList());
	}
	
	public OrderItemDTO buildOrderItemDTO(OrderItem o){
		return OrderItemDTO.builder()
				.id(o.getId())
				.order(o.getOrder())
				.inventory(o.getInventory())
				.quantity(o.getQuantity())
				.unitPrice(o.getUnitPrice())
				.subtotal(o.getSubtotal()).build();
	}
}
