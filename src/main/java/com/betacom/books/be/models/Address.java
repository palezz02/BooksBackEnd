package com.betacom.books.be.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "addresses")

public class Address {

	@Id
	@GeneratedValue ( strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "street", length = 100, nullable = false)
	private String street;
	
	@Column(name = "city", length = 50, nullable = false)
	private String city;
	
	@Column(name = "region", length = 50, nullable = true)
	private String region;
	
	@Column(name = "cap", length = 5, nullable = false)
	private String cap;
	
	@Column(name = "country", length = 50, nullable = false)
	private String country;
	
	@ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;
	
	@OneToMany(mappedBy = "address", fetch = FetchType.EAGER)
	private List<Order> orders;
}
