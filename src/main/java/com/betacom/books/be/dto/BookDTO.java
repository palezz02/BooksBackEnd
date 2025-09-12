package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookDTO {

	 private Integer id;
	    private String isbn;
	    private String title;
	    private Integer pageCount;
	    private String description;
	    private String coverImage;
	    private String languageCode;
	    private LocalDate publicationDate;
	    private String edition;
	    private PublisherDTO publisher;
	    private List<AuthorDTO> authors;
	    private List<CategoryDTO> categories;
	    private List<ReviewDTO> reviews;
}
