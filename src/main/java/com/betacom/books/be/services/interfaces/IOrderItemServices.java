package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.OrderItemReq;

public interface IOrderItemServices {
	List<OrderItemDTO> getAll() throws BooksException;
	OrderItemDTO getById(Integer id) throws BooksException;
	
	void create(OrderItemReq req) throws BooksException;
	void update(OrderItemReq req) throws BooksException;
	void delete(OrderItemReq req) throws BooksException;
	
	
}
