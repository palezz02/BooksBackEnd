package com.betacom.books.be.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "inentory_id", nullable = false)
	private Inventory inventory;

	@Min(0)
	@Max(99)
	@Column(nullable = false)
	private Integer quantity;

	@Min(0)
	@Column(name = "unit_price", nullable = false)
	private BigDecimal unitPrice;
	
	@Min(0)
	@Column(name = "subtotal", nullable = false)
	private BigDecimal subtotal;
}
