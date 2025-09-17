package com.betacom.books.be.Publisher;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.controller.PublisherController;
import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.requests.PublisherReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherControllerTest {
	
	@Autowired
	private PublisherController publisherC;
	
	
	
	@Test
	@Order(1)
	public void createPublisherTest() {
		log.debug("Test create Publisher");
		
		PublisherReq req = new PublisherReq();
		req.setName("test");
		req.setDescription("test");
		
		ResponseBase res = publisherC.create(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(2)
	public void updatePublisherTest() {
		log.debug("Test update Publisher");
		
		PublisherReq req = new PublisherReq();
		req.setId(1);
		req.setName("aa");
		
		ResponseBase res = publisherC.update(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(3)
	public void deletePublisherTest() {
		log.debug("Test update Publisher");
		
		PublisherReq req = new PublisherReq();
		req.setId(1);
		
		ResponseBase res = publisherC.delete(req);
		Assertions.assertThat(res.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(4)
	public void getByIdPublisher() {
		log.debug("Test get Publisher");
		
		ResponseObject<PublisherDTO> r = publisherC.getById(1);
		
		Assertions.assertThat(r.getRc()).isEqualTo(true);
	}
	
	@Test
	@Order(5)
	public void getAllPublisher() {
		log.debug("Test get Publisher");
		
		ResponseList<PublisherDTO> r = publisherC.getAll();
		
		Assertions.assertThat(r.getRc()).isEqualTo(true);
	}
}
