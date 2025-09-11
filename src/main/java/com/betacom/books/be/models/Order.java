package com.betacom.books.be.models;

import java.io.ObjectInputFilter.Status;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "status", nullable = false)
	private Status status;

	@Column(name = "total", nullable = false)
	private Integer total;

	@Column(name = "order_number", unique = true)
	private Integer orderNumber;

	@Column(name = "created_at", nullable = false)
	private LocalDate createdAt;

	@Column(name = "updated_at", nullable = false)
	private LocalDate updatedAt;

	@ManyToOne
	@JoinColumn(name = "shipping_address_id", nullable = false)
	private Address address;
	
	
	
	
	
}
