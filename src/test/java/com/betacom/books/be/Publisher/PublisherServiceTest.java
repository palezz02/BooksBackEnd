package com.betacom.books.be.Publisher;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.models.Publisher;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.utils.UtilsPublisher;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherServiceTest extends UtilsPublisher {
	
	@Autowired
	private IPublisherRepository publishRep;
	
	@Test
	@Order(1)
	public void createPublisherImplTest() {
		log.debug("Test create Publisher");
		
		Publisher p = new Publisher();
		p.setName("test");
		p.setDescription("test");
		
		publishRep.save(p);

	}
	
	@Test
	@Order(2)
	public void updatePublisherImplTest() {
		log.debug("Test update Publisher");
		
		Publisher p = new Publisher();
		p.setId(1);
		p.setDescription("testtest");
		
		publishRep.save(p);
	}
	
	@Test
	@Order(3)
	public void deletePublisherImplTest() {
		log.debug("Test delete Publisher");
		
		Publisher p = new Publisher();
		p.setId(1);
		
		publishRep.delete(p);
	}
	
	@Test
	@Order(4)
	public void getAllPublisherImplTest() {
		log.debug("Test getAll Publisher");
		
		List<Publisher> publisherL = publishRep.findAll();
		buildPublisherDTOList(publisherL);
	}
	
	@Test
	@Order(4)
	public void getByIdPublisherImplTest() {
		log.debug("Test getById Publisher");
		
		Publisher p = new Publisher();
		p.setName("test");
		p.setDescription("test");
		
		publishRep.save(p);

		Optional<Publisher> publisher = publishRep.findById(1);
		
		
		Assertions.assertThat(publisher.isPresent()).isEqualTo(true);
	}
}
