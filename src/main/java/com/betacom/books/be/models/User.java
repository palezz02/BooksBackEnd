package com.betacom.books.be.models;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.betacom.books.be.utils.Roles;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Email
	@Column(name = "email", nullable = false, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false, length = 50)
	private String password;
	
	@Column(name = "first_name", nullable = false, length = 100)
	private String firstName;
	
	@Column(name = "last_name", nullable = false, length = 100)
	private String lastName;
	
	@Column(name = "birth_date", nullable = true)
	private LocalDate birthDate;
	
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;
	
	@Column(name = "updated_at", nullable = false)
	private LocalDateTime updatedAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Roles role;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Review> reviews;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Address> addresses;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
	private List<Order> orders;
}