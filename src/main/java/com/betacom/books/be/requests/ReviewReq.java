package com.betacom.books.be.requests;


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
	private Integer bookId;
	private Integer userId;
	private Integer rating;
	private String title;
	private String body;

}
