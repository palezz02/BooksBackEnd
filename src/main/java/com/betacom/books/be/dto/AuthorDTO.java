package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class AuthorDTO {

	private Integer id;
    private String fullName;
    private String biography;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String coverImageUrl;
    private List<BookDTO> books;

}
