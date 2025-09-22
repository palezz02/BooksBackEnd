package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;

import com.betacom.books.be.utils.Status;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
	private Integer id;
	private Status status;
	private Integer total;
	private Integer orderNumber;
	private Integer shippingAddress;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private List<Integer> orderItem;
}
