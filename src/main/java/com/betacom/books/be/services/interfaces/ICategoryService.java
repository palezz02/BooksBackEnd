package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.CategoryReq;

public interface ICategoryService{

	CategoryDTO create(CategoryReq req) throws BooksException;
	void update(CategoryReq req) throws BooksException;
	void delete(CategoryReq req) throws BooksException;
	CategoryDTO getById(Integer id) throws BooksException;
	List<CategoryDTO> getAll();
}
