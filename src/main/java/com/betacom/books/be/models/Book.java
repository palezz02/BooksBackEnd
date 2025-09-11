package com.betacom.books.be.models;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "books")
@Getter
@Setter
public class Book {
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name = "isbn", unique = true, length = 13, nullable = false)
	private String isbn;
	
	@Column(name = "title", length = 150, nullable = false)
	private String title;
	
	@Max(value = 10000)
	@Column(name = "page_count", nullable = false)
	private Integer pageCount;
	
	@Column(name = "description", nullable = true, length = 5000)
	private String description;
	
	@Column(name = "cover_image", nullable = true, length = 2048)
	private String coverImage;
	
	@Column(name = "language_code", nullable = true, length = 2)
	private String languageCode;
	
	@Column(name = "publication_date", nullable = true)
	private LocalDate PublicationDate;
	
	@Column(name = "edition", nullable = true, length = 100)
	private String edition;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name = "book_autors",
			joinColumns = @JoinColumn(name="book_id"),
			inverseJoinColumns = @JoinColumn(name = "authors_id")
			
			)
	private List<Author> authors;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "publisher_id", nullable = false)
	private Publisher publisher;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
	    name = "book_category",
	    joinColumns = @JoinColumn(name = "book_id"),
	    inverseJoinColumns = @JoinColumn(name = "category_id")
	)
	private List<Category> categories;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "inventory_id", referencedColumnName = "id")
	private Inventory inventory;
	
	@OneToMany(mappedBy = "book", fetch = FetchType.EAGER)
	private List<Review> reviews;
	

}
