package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;

import com.betacom.books.be.models.Book;

public class AuthorDTO {

	private Integer id;
    private String fullName;
    private String biography;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String coverImageUrl;
    private List<Book> books;

}
