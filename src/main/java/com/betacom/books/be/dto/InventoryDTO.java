package com.betacom.books.be.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
	private LocalDateTime updatedAt;
	private Integer bookId;
	private List<Integer> orderItem;
}
