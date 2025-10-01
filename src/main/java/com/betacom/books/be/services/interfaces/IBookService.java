package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.dto.ReviewBookDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.BookReq;

public interface IBookService {

	BookDTO create(BookReq req) throws BooksException;
	void update(BookReq req) throws BooksException;
	void delete(BookReq req) throws BooksException;
	BookDTO getById(Integer id) throws BooksException;
	List<BookDTO> getAll();
	List<BookDTO> getBestByReviews(Integer limit, Integer offset);
	List<BookDTO> getBestByCategory(Integer limit, Integer offset);
	List<BookDTO> getBooksOrderedByName();
	List<ReviewBookDTO> getBookReviews(Integer BookId);
}
