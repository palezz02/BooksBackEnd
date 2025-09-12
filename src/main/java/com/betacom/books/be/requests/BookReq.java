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
public class BookReq {

	private Integer id;
	private String isbn;
	private String title;
	private Integer pageCount;
	private String description;
	private String coverImage;
	private String languageCode;
	private LocalDate PublicationDate;
	private String edition;
	private String errorMsg;
}
