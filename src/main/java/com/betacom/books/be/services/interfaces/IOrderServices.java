package com.betacom.books.be.services.interfaces;

import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.OrderReq;

public interface IOrderServices {
	OrderDTO getById(Integer id) throws BooksException;

	void create(OrderReq req) throws BooksException;

	void update(OrderReq req) throws BooksException;

	void delete(OrderReq req) throws BooksException;

}
