package com.betacom.books.be.requests;

import java.time.LocalDate;

import com.betacom.books.be.utils.Status;

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
	private Status status;
	private Integer total;
	private Integer orderNumber;
	private Integer shippingAddress;
	private LocalDate updatedAt;
}
