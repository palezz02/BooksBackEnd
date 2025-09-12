package com.betacom.books.be.services.implementations;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.requests.PublisherReq;
import com.betacom.books.be.services.interfaces.IPublisherServices;

public class PublisherImpl implements IPublisherServices {
	private IPublisherRepository publishRep;
	
	@Override
	public List<PublisherDTO> getAll() throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public PublisherDTO getById(Integer id) throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void create(PublisherReq req) throws BooksException {
		
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(PublisherReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(PublisherReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

	

}
