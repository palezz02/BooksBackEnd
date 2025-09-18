package com.betacom.books.be.services.implementations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.services.interfaces.IOrderServices;
import com.betacom.books.be.utils.UtilsOrder;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderImpl extends UtilsOrder implements IOrderServices {


	private IOrderRepository orderRepository;
	private IAddressRepository addR;
	
	public OrderImpl(IOrderRepository orderRepository,IAddressRepository addR) {
		this.orderRepository = orderRepository;
		this.addR = addR;
	}

	@Override
	public OrderDTO getById(Integer id) throws BooksException {
		log.debug("Order id:" + id);
		Optional<Order> order = orderRepository.findById(id);

		if (order.isEmpty())
			throw new BooksException("Order with id: " + id + " doesn't exist");

		Order o = order.get();

		return buildOrderDTO(o);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void create(OrderReq req) throws BooksException {
		log.debug("Create: " + req);
		Order o = new Order();
		Optional<Order> order = orderRepository.findById(req.getId());

		if (order.isPresent())
			throw new BooksException("Order with id: " + req.getId() + " already exists");
		
		o.setCreatedAt(LocalDate.now());
		
		if(req.getStatus() == null)
			throw new BooksException("Status mandatory");
		
		Optional<Address> a = addR.findById(req.getShippingAddress());
		if(a.isEmpty())
			throw new BooksException("ShippingAddress mandatory");
		if(req.getTotal() == null)
			throw new BooksException("Total mandatory");
		if(req.getOrderNumber() == null)
			throw new BooksException("Order number mandatory");
		if(req.getUpdatedAt() == null)
			throw new BooksException("Updated at mandatory");
		
		o.setTotal(req.getTotal());
		o.setOrderNumber(req.getOrderNumber());
		o.setUpdatedAt(req.getUpdatedAt());
		
		
		o.setAddress(a.get());
		orderRepository.save(o);

	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(OrderReq req) throws BooksException {
		log.debug("Update: " + req);
		
		Optional<Order> order = orderRepository.findById(req.getId());
		
		if(order.isEmpty())
			throw new BooksException("Order with id: " + req.getId() + " doesn't exist");

		Order o = new Order();
		
		if((req.getStatus() != null)) {
			o.setStatus(req.getStatus());
		}
		
		if((req.getTotal() != null)) {
			o.setTotal(req.getTotal());
		}
		
		if((req.getOrderNumber() != null)) {
			o.setOrderNumber(req.getOrderNumber());
		}
		
		if((req.getUpdatedAt() != null)) {
			o.setUpdatedAt(req.getUpdatedAt());
		}
		
		orderRepository.save(o);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(OrderReq req) throws BooksException {
		log.debug("Delete: " + req);
		
		Optional<Order> order = orderRepository.findById(req.getId());
		
		if (order.isEmpty())
			throw new BooksException("Order with id: " + req.getId() + " doesn't exist");

		if (!order.get().getOrderItems().isEmpty())
			throw new BooksException("Order with id: " + req.getId() + " doesn't have items");

		orderRepository.delete(order.get());
	}

	@Override
	public List<OrderDTO> getAll() {
		log.debug("Get all orders!");
		List<Order> orders = orderRepository.findAll();
		return UtilsOrder.buildOrderListDTO(orders);
	}

}
