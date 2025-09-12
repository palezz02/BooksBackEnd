package com.betacom.books.be.services.implementations;

import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.services.interfaces.IPublisherServices;

public class PublisherImpl implements IPublisherServices {
	private IPublisherRepository publishRep;

	@Override
	public PublisherDTO getAll(OrderItemReq req) throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void create(OrderItemReq req) throws BooksException {
		
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(OrderItemReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(OrderItemReq req) throws BooksException {
		// TODO Auto-generated method stub
		
	}

}
