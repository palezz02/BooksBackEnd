package com.betacom.books.be.services.implementations;

import java.util.List;

import org.springframework.stereotype.Service;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.CategoryReq;
import com.betacom.books.be.services.interfaces.ICategoryService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class CategoryImpl implements ICategoryService{@Override
	public CategoryDTO create(CategoryReq req) throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(CategoryReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(CategoryReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CategoryDTO getById(Integer id) throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<CategoryReq> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

}
