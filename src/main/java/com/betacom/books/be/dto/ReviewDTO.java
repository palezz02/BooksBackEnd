package com.betacom.books.be.dto;

import java.time.LocalDateTime;

import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.User;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class ReviewDTO {

	private Integer id;
	private Book book;
	private User user;
	private Integer rating;
	private String title;
	private String body;
	private LocalDateTime createdAt;
}
