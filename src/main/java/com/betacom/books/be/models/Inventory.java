package com.betacom.books.be.models;

import java.io.ObjectInputFilter.Status;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Inventories")
@Getter
@Setter
public class Inventory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Max(value = 1000)
	@Min(value = 0)
	@Column(name = "stock", nullable = false)
	private Integer stock;
	
	@Max(value = 5000)
	@Min(value = 0)
	@Column(name = "price", nullable = false)
	private BigDecimal price;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;
	
	@OneToOne(mappedBy = "inventory")
	private Book book;
	
	@OneToMany(mappedBy = "inventory", fetch = FetchType.EAGER)
	private List<OrderItem> orderItems;
	
}
