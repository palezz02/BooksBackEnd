package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.OrderItem;
public class UtilsInventory extends UtilsOrderItem{
	public static InventoryDTO buildInventoryDTO(Inventory i) {
		return InventoryDTO.builder()
				.id(i.getId())
				.stock(i.getStock())
				.price(i.getPrice())
				.updatedAt(i.getUpdatedAt())
				.bookId(i.getBook().getId())
				.orderItem(i.getOrderItems().stream().map(OrderItem::getId).collect(Collectors.toList()))
				.build();
	}

	public static List<InventoryDTO> buildInventoryListDTO(List<Inventory> inv){
		if (inv == null) {
            return Collections.emptyList();
        }
        return inv.stream()
                    .map(UtilsInventory::buildInventoryDTO)
                    .collect(Collectors.toList());
	}
}
