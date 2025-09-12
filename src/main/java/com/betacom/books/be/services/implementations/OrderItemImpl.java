package com.betacom.books.be.services.implementations;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.repositories.IOrderItemRepository;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.services.interfaces.IOrderItemServices;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public class OrderItemImpl implements IOrderItemServices {
	private IOrderItemRepository orderIRep;
	
	@Override
	public List<OrderItemDTO> getAll() throws BooksException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public OrderItemDTO getById(Integer id) throws BooksException {
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
