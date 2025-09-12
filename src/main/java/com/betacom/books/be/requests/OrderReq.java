package com.betacom.books.be.requests;

import java.time.LocalDate;

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
public class OrderReq {
	private Integer id;
	private String status;
	private Integer total;
	private Integer orderNumber;
	private LocalDate createdAt;
	private LocalDate updatedAt;
	private String errorMsg;
}
