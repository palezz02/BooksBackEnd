package com.betacom.books.be.services.interfaces;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.OrderItemReq;

public interface IPublisherServices {
	PublisherDTO getAll(OrderItemReq req) throws BooksException;
	
	void create(OrderItemReq req) throws BooksException;
	void update(OrderItemReq req) throws BooksException;
	void delete(OrderItemReq req) throws BooksException;
	
	
}
