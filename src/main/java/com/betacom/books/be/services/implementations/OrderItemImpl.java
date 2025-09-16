package com.betacom.books.be.services.implementations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IOrderItemRepository;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.services.interfaces.IOrderItemServices;
import com.betacom.books.be.utils.UtilsOrderItem;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderItemImpl extends UtilsOrderItem implements IOrderItemServices {
	private IOrderItemRepository orderIRep;
	private IOrderRepository orderR;
	private IInventoryRepository invR;
	public OrderItemImpl(IOrderItemRepository orderIRep, IOrderRepository orderR,IInventoryRepository invR) {
		this.orderIRep = orderIRep;
		this.orderR = orderR;
		this.invR = invR;
	}
	
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
		
		
		if (req.getOrderId() == null)
			throw new BooksException("Order obbligatorio");
		if (req.getInventoryId() == null)
			throw new BooksException("Inventory obbligatorio");
		if (req.getQuantity() == null)
			throw new BooksException("Quantity obbligatorio");
		if (req.getUnitPrice() == null)
			throw new BooksException("UnitPrice obbligatorio");
		
		BigDecimal quantity = new BigDecimal(req.getQuantity());
		BigDecimal subTotal = quantity.multiply(req.getUnitPrice());
		
		Optional<Order> order = orderR.findById(req.getOrderId());
		if(order.isEmpty()) throw new BooksException("Order non trovato");
		
		o.setOrder(order.get());
		
		Optional<Inventory> inventory = invR.findById(req.getInventoryId());
		if(inventory.isEmpty()) throw new BooksException("Inventory non trovato");
		
		o.setInventory(inventory.get());
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
		Optional<Order> order = orderR.findById(req.getOrderId());
		if(order.isPresent()) {
			o.setOrder(order.get());
		}
		Optional<Inventory> inventory = invR.findById(req.getInventoryId());
		if(inventory.isPresent()) {
			o.setInventory(inventory.get());
		}
			
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
