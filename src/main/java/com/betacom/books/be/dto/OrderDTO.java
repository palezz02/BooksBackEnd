package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class OrderDTO {
	private Integer id;
	private String status;
	private Integer total;
	private Integer orderNumber;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private List<OrderItemDTO> orderItem;
}
