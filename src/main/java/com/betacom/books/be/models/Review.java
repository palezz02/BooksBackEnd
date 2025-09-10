package com.betacom.books.be.models;


import java.time.LocalDate;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table (name="reviews")
public class Review {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne
	@JoinColumn(
			name="book",
			nullable = false
			)
	private Books book;
	
	@ManyToOne
	@JoinColumn(
			name="user",
			nullable = false
			)
	private User user;
	
   @Min(0)
   @Max(5)
	@Column (name="rating",
			nullable = false)
	private int rating;
  
	@Column (name="title",
			nullable = true)
	private int title;
	
	@Column (name="body",
			nullable = true)
	private int body;
	
	@Column (name="created_at",
			nullable = false)
	private int createdAt;
	
	
	
}
