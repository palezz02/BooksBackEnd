package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.models.OrderItem;

public class UtilsOrderItem {
	public static List<OrderItemDTO> buildOrderItemListDTO(List<OrderItem> lista){
		if (lista == null) {
            return Collections.emptyList();
        }
        return lista.stream()
                    .map(UtilsOrderItem::buildOrderItemDTO)
                    .collect(Collectors.toList());
	}
	
	public static OrderItemDTO buildOrderItemDTO(OrderItem o){
		return OrderItemDTO.builder()
				.id(o.getId())
				.orderId(o.getOrder().getId())
				.inventory(o.getInventory().getId())
				.quantity(o.getQuantity())
				.unitPrice(o.getUnitPrice())
				.subtotal(o.getSubtotal()).build();
	}
}
