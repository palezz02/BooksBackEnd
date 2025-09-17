package com.betacom.books.be.OrderItem;

import java.math.BigDecimal;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.controller.OrderItemController;
import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemControllerTest {
	
	@Autowired
	private OrderItemController orderIC;
	
	
	
	@Test
	@Order(1)
	public void createOrderItemTest() {
		log.debug("Test create OrderItem");
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(12);
		
		BigDecimal uPrice = new BigDecimal(3.99);
		req.setUnitPrice(uPrice);
		BigDecimal quantity = new BigDecimal(12);
		BigDecimal total = quantity.multiply(uPrice);
		
		req.setSubtotal(total);
		
		ResponseBase res = orderIC.create(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(2)
	public void updateOrderItemTest() {
		log.debug("Test update OrderItem");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(1);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(12);
		
		BigDecimal uPrice = new BigDecimal(3.99);
		req.setUnitPrice(uPrice);
		BigDecimal quantity = new BigDecimal(12);
		BigDecimal total = quantity.multiply(uPrice);
		
		req.setSubtotal(total);
		
		ResponseBase res = orderIC.update(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(3)
	public void deleteOrderItemTest() {
		log.debug("Test update OrderItem");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(1);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(12);
		
		BigDecimal uPrice = new BigDecimal(3.99);
		req.setUnitPrice(uPrice);
		BigDecimal quantity = new BigDecimal(12);
		BigDecimal total = quantity.multiply(uPrice);
		
		req.setSubtotal(total);
		
		ResponseBase res = orderIC.delete(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(4)
	public void getByIdOrderItem() {
		log.debug("Test get OrderItem");
		
		ResponseObject<OrderItemDTO> r = orderIC.getById(1);
		
		Assertions.assertThat(r.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(5)
	public void getAllOrderItem() {
		log.debug("Test get OrderItem");
		
		ResponseList<OrderItemDTO> r = orderIC.getAll();
		
		Assertions.assertThat(r.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(6)
	public void errorTest() {
		log.debug("Test Errors OrderItem");
		// GetById error
		ResponseObject<OrderItemDTO> r = orderIC.getById(-1);
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		
		// Create error
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(12);
		
		ResponseBase res = orderIC.create(req);
		Assertions.assertThat(res.getRc()).isEqualTo(false);
		
		// Delete error
		OrderItemReq req2 = new OrderItemReq();
		req.setId(-1);
		ResponseBase res2 = orderIC.delete(req2);
		Assertions.assertThat(res2.getRc()).isEqualTo(false);
	}
}
