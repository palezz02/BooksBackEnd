package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.PublisherReq;


public interface IPublisherServices {
	List<PublisherDTO> getAll() throws BooksException;
	PublisherDTO getById(Integer id) throws BooksException;
	
	void create(PublisherReq req) throws BooksException;
	void update(PublisherReq req) throws BooksException;
	void delete(PublisherReq req) throws BooksException;
	
	
}
