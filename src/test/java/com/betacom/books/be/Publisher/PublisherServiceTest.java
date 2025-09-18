package com.betacom.books.be.Publisher;

import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.models.Publisher;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.requests.PublisherReq;
import com.betacom.books.be.services.interfaces.IPublisherServices;
import com.betacom.books.be.utils.UtilsPublisher;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PublisherServiceTest extends UtilsPublisher {
	
	@Autowired
	private IPublisherServices publisherS;
	
	@Test
	@Order(1)
	public void createPublisherTest() throws BooksException {
		log.debug("Test createPublisherTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test");
		req.setDescription("test");
		
		Assertions.assertThat(publisherS.create(req)).isExactlyInstanceOf(PublisherDTO.class);	
	}
	
	@Test
	@Order(2)
	public void createPublisherSameNameTest() throws BooksException {
		log.debug("Test createPublisherSameNameTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test2");
		req.setDescription("test2");
		
		publisherS.create(req);
		
		PublisherReq req2 = new PublisherReq();
		
		req2.setName("test2");
		req2.setDescription("test2");
		
		Assertions.assertThatThrownBy(() -> publisherS.create(req2)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(3)
	public void createNullDescriptionTest() throws BooksException {
		log.debug("Test createNullDescriptionTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test3");
		req.setDescription(null);
		
		Assertions.assertThatThrownBy(() -> publisherS.create(req)).isInstanceOf(BooksException.class);
		
		
	}
	
	@Test
	@Order(4)
	public void createNullNameTest() throws BooksException {
		log.debug("Test createNullNameTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName(null);
		req.setDescription("test4");
		
		Assertions.assertThatThrownBy(() -> publisherS.create(req)).isInstanceOf(BooksException.class);
		
		
	}
	
	
	@Test
	@Order(5)
	public void updateNameTest() throws BooksException {
		log.debug("Test updateNameTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test5");
		req.setDescription("test5");
		Integer id = publisherS.create(req).getId();
		
		PublisherReq req2 = new PublisherReq();
		req2.setId(id);
		req2.setName("testUpdated");
		publisherS.update(req2);

		PublisherDTO p = publisherS.getById(id);
		Assertions.assertThat(p.getName()).isEqualTo("testUpdated");
		
	}
	
	@Test
	@Order(6)
	public void updateDescTest() throws BooksException {
		log.debug("Test updateDescTest Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test6");
		req.setDescription("test6");
		Integer id = publisherS.create(req).getId();
		
		PublisherReq req2 = new PublisherReq();
		req2.setId(id);
		req2.setDescription("testUpdatedDesc");
		publisherS.update(req2);

		PublisherDTO p = publisherS.getById(id);
		Assertions.assertThat(p.getDescription()).isEqualTo("testUpdatedDesc");
		
	}
	
	@Test
	@Order(7)
	public void updatePublisherNotFoundTest() throws BooksException {
		log.debug("Test updateDescTest Publisher");
		Integer id = 100;
		
		PublisherReq req = new PublisherReq();
		req.setId(id);
		req.setDescription("testUpdatedDesc");
		
		Assertions.assertThatThrownBy(() -> publisherS.update(req)).isInstanceOf(BooksException.class);
	}
	
	
	@Test
	@Order(8)
	public void deletePublisherImplTest() throws BooksException {
		log.debug("Test delete Publisher");
		PublisherReq req = new PublisherReq();
		
		req.setName("test8");
		req.setDescription("test8");
		publisherS.create(req);
		publisherS.delete(req);

		Assertions.assertThatThrownBy(() -> publisherS.getById(req.getId())).isInstanceOf(BooksException.class);
		
	}
	
	@Test
	@Order(9)
	public void deletePublisherNotFoundTest() throws BooksException {
		log.debug("Test delete Publisher");
		PublisherReq req2 = new PublisherReq();
		
		req2.setId(22222);
		Assertions.assertThatThrownBy(() -> publisherS.delete(req2)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(10)
	public void getAllPublisherImplTest() throws BooksException {
		log.debug("Test getAll Publisher");
		
		List<PublisherDTO> publisherL = publisherS.getAll();
		Assertions.assertThat(publisherL)
        .isNotNull()
        .isNotEmpty();
	}
	
	@Test
	@Order(11)
	public void getByIdPublisherImplTest() throws BooksException {
		log.debug("Test getById Publisher");
		
		PublisherReq p = new PublisherReq();
		p.setName("test11");
		p.setDescription("test11");
		
		publisherS.create(p);
		Assertions.assertThat(publisherS.getById(p.getId())).isExactlyInstanceOf(PublisherDTO.class);	
	}
	

	@Test
	@Order(12)
	public void getByIdNotFound() throws BooksException {
		log.debug("Test getById Publisher");
		
		Assertions.assertThat(publisherS.getById(null)).isExactlyInstanceOf(BooksException.class);	
	}
}
