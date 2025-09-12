package com.betacom.books.be.utils;

import java.util.List;

import com.betacom.books.be.dto.ReviewDTO;
import com.betacom.books.be.models.Review;

public class UtilitiesReview {

	private UtilitiesReview() {
	}

	public static ReviewDTO toDTO(Review rev) {
		if (rev == null) {
			return null;
		}

		return ReviewDTO.builder().id(rev.getId()).book(rev.getBook())
				.user(rev.getUser()) 
				.rating(rev.getRating()).title(rev.getTitle()).body(rev.getBody()).createdAt(rev.getCreatedAt())
				.build();
	}
	
	public static List<ReviewDTO> toDTOList(List<Review> reviews) {
	    if (reviews == null) {
	        return List.of();
	    }
	    return reviews.stream()
	                  .map(UtilitiesReview::toDTO)
	                  .toList();
	}

}
