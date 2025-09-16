package com.betacom.books.be.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class InventoryDTO {
	private Integer id;
	private Integer stock;
	private BigDecimal price;
	private LocalDate updatedAt;
	private BookDTO book;
	private List<OrderItemDTO> orderItem;
}
