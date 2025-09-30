package com.betacom.books.be.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ReviewBookDTO {
	private int reviewId;
    private int rating;
    private Timestamp createdAt;
    private String body;
    private String reviewTitle;
    private int userId;
    private String firstName;
    private String lastName;
}
