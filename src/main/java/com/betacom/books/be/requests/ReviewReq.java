package com.betacom.books.be.requests;

import java.time.LocalDateTime;

import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.User;

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
public class ReviewReq {

	private Integer id;
	private Book book;
	private User user;
	private Integer rating;
	private String title;
	private String body;
	private LocalDateTime createdAt;
	private String errrorMsg;

}
