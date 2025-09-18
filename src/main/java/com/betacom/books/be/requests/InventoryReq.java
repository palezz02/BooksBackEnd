package com.betacom.books.be.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class InventoryReq {
	private Integer id;
	private Integer stock;
	private BigDecimal price;
	private LocalDateTime updatedAt;
	private String errorMsg;
}
