package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.ReviewDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.ReviewReq;

public interface IReviewServices {

	ReviewDTO create(ReviewReq req) throws BooksException;

	void delete(ReviewReq req) throws BooksException;

	void update(ReviewReq req) throws BooksException;

	List<ReviewDTO> getAll();
}
