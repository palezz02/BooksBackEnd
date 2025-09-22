package com.betacom.books.be.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.controller.OrderItemController;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.implementations.OrderImpl;
import com.betacom.books.be.utils.Roles;
import com.betacom.books.be.utils.Status;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemControllerTest {

    @Autowired
    private OrderItemController orderItemC;
    
    @Autowired
	private OrderImpl orderI;
	
	@Autowired
	private IInventoryRepository invRepo;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IAddressRepository addR;
	
	@Test
	@Order(1)
	public void createOrderItemTest() throws BooksException {
	    log.debug("Test create OrderItem");

	    // 1. Creo un utente valido
	    User u = new User();
	    u.setFirstName("test");
	    u.setLastName("test");
	    u.setEmail("test@example.com");
	    u.setBirthDate(LocalDate.now());
	    u.setCreatedAt(LocalDateTime.now());
	    u.setUpdatedAt(LocalDateTime.now());
	    u.setPassword("test");
	    u.setRole(Roles.CUSTOMER);
	    userRepository.save(u);

	    // 2. Creo un indirizzo valido
	    Address addr = new Address();
	    addr.setStreet("Via Roma 1");
	    addr.setCity("Napoli");
	    addr.setCap("80100");
	    addr.setCountry("test");
	    addr.setRegion("test");
	    addr.setUser(u);
	    addR.save(addr);

	    // 3. Creo l’ordine valido
	    OrderReq o = new OrderReq();
	    o.setId(1);
	    o.setStatus(Status.PROCESSING);
	    o.setTotal(20);
	    o.setOrderNumber(1);
	    o.setUpdatedAt(LocalDate.now());
	    o.setShippingAddress(addr.getId());
	    o.setUser(u.getId());

	    OrderDTO order = orderI.create(o);

	    // 4. Creo un inventario valido
	    Inventory i = new Inventory();
	    i.setStock(100);
	    i.setPrice(new BigDecimal("10.00"));
	    i.setUpdatedAt(LocalDateTime.now());
	    invRepo.save(i);

	    // 5. Creo l’OrderItem
	    OrderItemReq req = new OrderItemReq();
	    req.setId(1);
	    req.setOrderId(order.getId());
	    req.setInventoryId(i.getId());
	    req.setQuantity(5);

	    ResponseBase res = orderItemC.create(req);

	    // 6. Asserzione finale
	    Assertions.assertThat(res.getRc()).isEqualTo(true);
	}


    @Test
    @Order(2)
    public void updateOrderItemTest() throws BooksException {
        log.debug("Test update OrderItem");

        
        OrderItemReq req = new OrderItemReq();
        req.setId(1);
        req.setOrderId(1);         
        req.setInventoryId(1);     
        req.setQuantity(1);

        ResponseBase res = orderItemC.update(req);
        Assertions.assertThat(res.getRc()).isEqualTo(true);
    }

    @Test
    @Order(3)
    public void getByIdOrderItemTest() {
        log.debug("Test get OrderItem by ID");

        ResponseObject<OrderItemDTO> res = orderItemC.getById(1);
        Assertions.assertThat(res.getRc()).isEqualTo(true);
    }

    @Test
    @Order(4)
    public void deleteOrderItemTest() {
        log.debug("Test delete OrderItem");

        OrderItemReq req = new OrderItemReq();
        req.setId(1);
        req.setInventoryId(1);
        req.setQuantity(1);
        req.setOrderId(1);
        
        ResponseBase res = orderItemC.delete(req);
        Assertions.assertThat(res.getRc()).isEqualTo(true);
    }

    @Test
    @Order(5)
    public void getAllOrderItemTest() {
        log.debug("Test get all OrderItems");

        ResponseList<OrderItemDTO> res = orderItemC.getAll();
        Assertions.assertThat(res.getRc()).isEqualTo(true);
    }

    @Test
    @Order(6)
    public void errorGetByIdTest() {
        log.debug("Test GetById error OrderItem");

        ResponseObject<OrderItemDTO> res = orderItemC.getById(999); // ID inesistente
        Assertions.assertThat(res.getRc()).isEqualTo(false);
    }

    @Test
    @Order(7)
    public void errorUpdateTest() {
        log.debug("Test Update error OrderItem");

        OrderItemReq req = new OrderItemReq();
        req.setId(999);    
        req.setOrderId(1);

        ResponseBase res = orderItemC.update(req);
        Assertions.assertThat(res.getRc()).isEqualTo(false);
    }

    @Test
    @Order(8)
    public void errorCreateTest() {
        log.debug("Test Create error OrderItem");

        OrderItemReq req = new OrderItemReq();
        // Mancano tutti i dati obbligatori
        ResponseBase res = orderItemC.create(req);
        Assertions.assertThat(res.getRc()).isEqualTo(false);
    }

    @Test
    @Order(9)
    public void errorDeleteTest() {
        log.debug("Test Delete error OrderItem");

        OrderItemReq req = new OrderItemReq();
        req.setId(999); // ID inesistente
        ResponseBase res = orderItemC.delete(req);
        Assertions.assertThat(res.getRc()).isEqualTo(false);
    }
    
   
}
