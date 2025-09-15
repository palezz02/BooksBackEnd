package com.betacom.books.be.services.implementations;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.betacom.books.be.configurations.WebSecurityConfiguration;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.services.interfaces.IOrderServices;
import com.betacom.books.be.utils.Status;
import com.betacom.books.be.utils.Utilities;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class OrderImpl extends Utilities implements IOrderServices {

    private final WebSecurityConfiguration webSecurityConfiguration;

	private IOrderRepository orderRepository;

	public OrderImpl(IOrderRepository orderRepository, WebSecurityConfiguration webSecurityConfiguration) {
		this.orderRepository = orderRepository;
		this.webSecurityConfiguration = webSecurityConfiguration;
	}

	@Override
	public OrderDTO getById(Integer id) throws BooksException {
		log.debug("Order id:" + id);
		Optional<Order> order = orderRepository.findById(id);

		if (order.isEmpty())
			throw new BooksException("Order with id: " + id + " doesn't exist");

		Order o = order.get();

		return OrderDTO.builder()
				.id(o.getId())
				.status(o.getStatus().toString())
				.total(o.getTotal())
				.orderNumber(o.getOrderNumber())
				.createdAt(o.getCreatedAt())
				.updatedAt(o.getUpdatedAt())
				.orderItem(buildOrderItems(o.getOrderItems()))
				.build();
	}

	@Override
	public void create(OrderReq req) throws BooksException {
		log.debug("Create: " + req);
		Order o = new Order();
		Optional<Order> order = orderRepository.findById(req.getId());

		if (order.isPresent())
			throw new BooksException("Order with id: " + req.getId() + " already exists");

		if(req.getStatus() == null)
			throw new BooksException("Status mandatory");
		if(req.getTotal() == null)
			throw new BooksException("Total mandatory");
		if(req.getOrderNumber() == null)
			throw new BooksException("Order number mandatory");
		if(req.getCreatedAt() == null)
			throw new BooksException("Created at mandatory");
		if(req.getUpdatedAt() == null)
			throw new BooksException("Updated at mandatory");
		
		o.setId(req.getId());
		o.setTotal(req.getTotal());
		o.setOrderNumber(req.getOrderNumber());
		o.setCreatedAt(req.getCreatedAt());
		o.setUpdatedAt(req.getUpdatedAt());
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
			o.setStatus(Status.valueOf(req.getStatus()));
		}
		
		if((req.getTotal() != null)) {
			o.setTotal(req.getTotal());
		}
		
		if((req.getOrderNumber() != null)) {
			o.setOrderNumber(req.getOrderNumber());
		}
		
		if((req.getCreatedAt() != null)) {
			o.setCreatedAt(req.getCreatedAt());
		}
		
		if((req.getUpdatedAt() != null)) {
			o.setUpdatedAt(req.getUpdatedAt());
		}
		
		orderRepository.save(o);
	}

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

}
