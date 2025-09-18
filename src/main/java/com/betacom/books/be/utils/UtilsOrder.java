package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.OrderItem;

public class UtilsOrder  {
	public static OrderDTO buildOrderDTO(Order o) {
		if (o == null) {
			return null;
		}

		return OrderDTO.builder()
				.id(o.getId())
				.status(o.getStatus())
				.total(o.getTotal())
				.orderNumber(o.getOrderNumber())
				.createdAt(o.getCreatedAt())
				.updatedAt(o.getUpdatedAt())
				.orderItem(o.getOrderItems() != null ? 
						o.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList()) : 
						Collections.emptyList())
				.shippingAddress(o.getAddress() != null ? o.getAddress().getId() : null)
				.build();
	}

public static List<OrderDTO> buildOrderListDTO(List<Order> orders){
	if (orders == null) {
        return Collections.emptyList();
    }
    return orders.stream()
                .map(UtilsOrder::buildOrderDTO)
                .collect(Collectors.toList());
}
}
