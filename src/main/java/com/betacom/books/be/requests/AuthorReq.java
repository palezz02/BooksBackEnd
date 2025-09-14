package com.betacom.books.be.requests;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AuthorReq {

	private Integer id;
    private String fullName;
    private String biography;
    private LocalDate birthDate;
    private LocalDate deathDate;
    private String coverImageUrl;

}
