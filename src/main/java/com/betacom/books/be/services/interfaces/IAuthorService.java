package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.AuthorReq;

public interface IAuthorService {

	Integer create(AuthorReq req) throws BooksException;

	void delete(AuthorReq req) throws BooksException;

	void update(AuthorReq req) throws BooksException;

	AuthorDTO getById(Integer id) throws BooksException;

	List<AuthorDTO> getAll();

}
