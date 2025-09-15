package com.betacom.books.be.services.implementations;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.repositories.IOrderItemRepository;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.services.interfaces.IOrderItemServices;
import com.betacom.books.be.utils.UtilsOrderItem;

import lombok.extern.log4j.Log4j2;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
public class OrderItemImpl extends UtilsOrderItem implements IOrderItemServices {
	private IOrderItemRepository orderIRep;
	
	@Override
	public List<OrderItemDTO> getAll() throws BooksException {
		log.debug("getAll OrderItem");
		List<OrderItem> orderItemL = orderIRep.findAll();
		return buildOrderItemListDTO(orderItemL);
	}
	
	@Override
	public OrderItemDTO getById(Integer id) throws BooksException {
		log.debug("getById OrderItem");
		Optional<OrderItem> orderItem = orderIRep.findById(id);
		
		if(orderItem.isEmpty()) {
			throw new BooksException("OrderItem non trovato");
		}
		
		OrderItem o = orderItem.get();
		
		return buildOrderItemDTO(o);
	}
	
	@Transactional(rollbackFor = Exception.class)
	@Override
	public OrderItemDTO create(OrderItemReq req) throws BooksException {
		log.debug("create OrderItem");
		OrderItem o = new OrderItem();
		Optional<OrderItem> orderItem = orderIRep.findById(req.getId());
		
		if(orderItem.isPresent()) {
			throw new BooksException("OrderItem esistente");
		}
		
		
		if (req.getOrder() == null)
			throw new BooksException("Order obbligatorio");
		if (req.getInventory() == null)
			throw new BooksException("Inventory obbligatorio");
		if (req.getQuantity() == null)
			throw new BooksException("Quantity obbligatorio");
		if (req.getUnitPrice() == null)
			throw new BooksException("UnitPrice obbligatorio");
		
		BigDecimal quantity = new BigDecimal(req.getQuantity());
		BigDecimal subTotal = quantity.multiply(req.getUnitPrice());
		
		
		o.setOrder(req.getOrder());
		o.setInventory(req.getInventory());
		o.setQuantity(req.getQuantity());
		o.setUnitPrice(req.getUnitPrice());
		o.setSubtotal(subTotal);
		
		orderIRep.save(o);
		
		return buildOrderItemDTO(o);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(OrderItemReq req) throws BooksException {
		log.debug("update OrderItem");
		Boolean modified = false;
		OrderItem o = new OrderItem();
		Optional<OrderItem> orderItem = orderIRep.findById(req.getId());
		
		if(orderItem.isEmpty()) {
			throw new BooksException("OrderItem non trovato");
		}
		
		if (req.getOrder() != null)
			o.setOrder(req.getOrder());
		if (req.getInventory() != null)
			o.setInventory(req.getInventory());
		if (req.getQuantity() != null) {
			o.setQuantity(req.getQuantity());
			modified = true;
		}
			
		if (req.getUnitPrice() != null) {
			o.setUnitPrice(req.getUnitPrice());
			modified = true;
		}
			
		if(modified == true) {
			BigDecimal quantity = new BigDecimal(req.getQuantity());
			BigDecimal subTotal = quantity.multiply(req.getUnitPrice());
			o.setSubtotal(subTotal);
		}
		
		orderIRep.save(o);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(OrderItemReq req) throws BooksException {
		log.debug("delete OrderItem");
		Optional<OrderItem> orderItem = orderIRep.findById(req.getId());
		
		if(orderItem.isEmpty()) {
			throw new BooksException("OrderItem non trovato");
		}
		
		orderIRep.delete(orderItem.get());
	}

	

}
