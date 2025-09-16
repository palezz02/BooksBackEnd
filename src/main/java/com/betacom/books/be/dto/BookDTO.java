package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
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
	    private Integer publisher;
	    private List<Integer> authors;
	    private List<CategoryDTO> categories;
	    private List<Integer> reviews;
}
