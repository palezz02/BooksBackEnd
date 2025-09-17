package com.betacom.books.be.OrderItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IOrderItemRepository;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.utils.UtilsOrderItem;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemServiceTest extends UtilsOrderItem {
	
	@Autowired
	private IOrderItemRepository orderIRep;
	@Autowired
	private IInventoryRepository invR;
	
	@Test
	@Order(1)
	public void createOrderItemImplTest() {
		log.debug("Test create OrderItem");
		
		OrderItem req = new OrderItem();
		BigDecimal uPrice = new BigDecimal(3.99);
		BigDecimal quantity = new BigDecimal(12);
		BigDecimal total = quantity.multiply(uPrice);
		Optional<Inventory> inventory = invR.findById(1);
		req.setInventory(inventory.get());
		req.setQuantity(1);
		req.setUnitPrice(uPrice);
		req.setSubtotal(total);
		
		orderIRep.save(req);

	}
	
	@Test
	@Order(2)
	public void updateOrderItemImplTest() {
		log.debug("Test update OrderItem");
		
		OrderItem req = new OrderItem();
		Optional<Inventory> inventory = invR.findById(2);
		req.setId(1);
		req.setInventory(inventory.get());
		
		orderIRep.save(req);
	}
	
	@Test
	@Order(3)
	public void deleteOrderItemImplTest() {
		log.debug("Test delete OrderItem");
		
		OrderItem req = new OrderItem();
		req.setId(1);
		
		orderIRep.delete(req);
	}
	
	@Test
	@Order(4)
	public void getAllOrderItemImplTest() {
		log.debug("Test getAll OrderItem");
		
		List<OrderItem> orderItemL = orderIRep.findAll();
		buildOrderItemListDTO(orderItemL);
	}
	
	@Test
	@Order(4)
	public void getByIdOrderItemImplTest() {
		log.debug("Test getById OrderItem");
		
		OrderItem req = new OrderItem();
		BigDecimal uPrice = new BigDecimal(3.99);
		BigDecimal quantity = new BigDecimal(12);
		BigDecimal total = quantity.multiply(uPrice);
		Optional<Inventory> inventory = invR.findById(1);
		req.setInventory(inventory.get());
		req.setQuantity(1);
		req.setUnitPrice(uPrice);
		req.setSubtotal(total);
		
		Optional<OrderItem> orderItem = orderIRep.findById(1);
		
		
		Assertions.assertThat(orderItem.isPresent()).isEqualTo(true);
	}
}
