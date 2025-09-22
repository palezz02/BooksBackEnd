package com.betacom.books.be.OrderItem;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.AddressReq;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.services.implementations.AddressImpl;
import com.betacom.books.be.services.implementations.OrderImpl;
import com.betacom.books.be.services.implementations.UserImpl;
import com.betacom.books.be.services.interfaces.IAddressService;
import com.betacom.books.be.services.interfaces.IOrderItemServices;
import com.betacom.books.be.utils.Roles;
import com.betacom.books.be.utils.Status;
import com.betacom.books.be.utils.UtilsOrderItem;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderItemServiceTest extends UtilsOrderItem {
	
	@Autowired
	private IOrderItemServices orderItemS;
	
	
	@Autowired
	private UserImpl userI;
	
	@Autowired
	private OrderImpl orderI;
	
	@Autowired
	private AddressImpl addI;
	
	@Autowired
	private IInventoryRepository invRepo;
	
	@Autowired
	private IAddressService addS;
	
	@Autowired
	private IUserRepository userRepository;
	
	@Autowired
	private IAddressRepository addR;
	
	private OrderDTO createOrder() throws BooksException {
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

	    return orderI.create(o);
	}
	
	private Inventory createInventory() {
		Inventory i = new Inventory();
		i.setStock(100);
		i.setPrice(new BigDecimal("10.00"));
		i.setUpdatedAt(LocalDateTime.now());
		return invRepo.save(i);
	}
	
	
	@Test
	@Order(1)
	public void createOrderItemTest() throws BooksException {
		log.debug("Test createOrderItemTest");
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setId(1);
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(2);
		
		Assertions.assertThat(orderItemS.create(req)).isExactlyInstanceOf(OrderItemDTO.class);	
	}
	
	@Test
	@Order(2)
	public void createOrderItemExistingIdTest() throws BooksException {
		log.debug("Test createOrderItemExistingIdTest");
		
		OrderItemReq req = new OrderItemReq(); 
		req.setId(9);
		req.setOrderId(1); 
		req.setInventoryId(1); 
		req.setQuantity(1); 
		OrderItemDTO dto = orderItemS.create(req); 
		OrderItemReq req2 = new OrderItemReq(); 
		req.setId(10);
		req2.setId(dto.getId()); 
		req2.setOrderId(1); 
		req2.setInventoryId(1); 
		req2.setQuantity(3);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req2)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(3)
	public void createOrderItemNullOrderTest() throws BooksException {
		log.debug("Test createOrderItemNullOrderTest");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(2);
		req.setOrderId(null);
		req.setInventoryId(1);
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(4)
	public void createOrderItemNullInventoryTest() throws BooksException {
		log.debug("Test createOrderItemNullInventoryTest");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(3);
		req.setOrderId(1);
		req.setInventoryId(null);
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(5)
	public void updateQuantityTest() throws BooksException {
		log.debug("Test updateQuantityTest");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(4);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(2);
		Integer id = orderItemS.create(req).getId();
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(id);
		req2.setOrderId(1);
		req2.setInventoryId(1);
		req2.setQuantity(5);
		orderItemS.update(req2);

		OrderItemDTO o = orderItemS.getById(id);
		Assertions.assertThat(o.getQuantity()).isEqualTo(5);
	}
	
	@Test
	@Order(6)
	public void updateQuantityInvertedInventory() throws BooksException {
		log.debug("Test updateUnitPriceTest");
		

		OrderItemReq req = new OrderItemReq();
		req.setId(16);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(2);
		Integer id = orderItemS.create(req).getId();
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(id);
		req2.setOrderId(1);
		req2.setInventoryId(1);
		req2.setQuantity(1);
		orderItemS.update(req2);

		OrderItemDTO o = orderItemS.getById(id);
		Assertions.assertThat(o.getQuantity()).isEqualTo(1);
	}
	
	@Test
	@Order(6)
	public void errorUpdateInventoryNotEnoughCopy() throws BooksException {
		log.debug("Test updateUnitPriceTest");
		

		OrderItemReq req = new OrderItemReq();
		req.setId(16);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(2);
		Integer id = orderItemS.create(req).getId();
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(id);
		req2.setOrderId(1);
		req2.setInventoryId(1);
		req2.setQuantity(50000);
		Assertions.assertThatThrownBy(() -> orderItemS.update(req2)).isInstanceOf(BooksException.class);
	}
	
	
	
	
	@Test
	@Order(7)
	public void updateOrderItemNotFoundTest() throws BooksException {
		log.debug("Test updateOrderItemNotFoundTest");
		OrderItemReq req = new OrderItemReq();
		req.setId(6);
		req.setId(99999);
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.update(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(8)
	public void deleteOrderItemTest() throws BooksException {
		log.debug("Test deleteOrderItemTest");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(1);
		OrderItemDTO o = orderItemS.create(req);
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(o.getId());
		req2.setOrderId(o.getOrderId());
		req2.setInventoryId(o.getInventory());
		req2.setQuantity(o.getQuantity());
		orderItemS.delete(req2);

		Assertions.assertThatThrownBy(() -> orderItemS.getById(o.getId())).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(9)
	public void deleteOrderItemNotFoundTest() throws BooksException {
		log.debug("Test deleteOrderItemNotFoundTest");
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(22222);
		Assertions.assertThatThrownBy(() -> orderItemS.delete(req2)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(10)
	public void getAllOrderItemTest() throws BooksException {
		log.debug("Test getAllOrderItemTest");
		List<OrderItemDTO> orderItemL = orderItemS.getAll();
		Assertions.assertThat(orderItemL).isNotNull();
	}
	
	@Test
	@Order(11)
	public void getByIdOrderItemTest() throws BooksException {
		log.debug("Test getByIdOrderItemTest");
		
		OrderItemReq req = new OrderItemReq();
		req.setId(12);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(2);
		
		OrderItemDTO oD = orderItemS.create(req);
		Assertions.assertThat(orderItemS.getById(oD.getId())).isInstanceOf(OrderItemDTO.class);	
	}
	
	@Test
	@Order(12)
	public void getByIdNotFound() throws BooksException {
		log.debug("Test getByIdNotFound");
		Assertions.assertThatThrownBy(() -> orderItemS.getById(222)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(13)
	public void deleteInventoryNotFound() throws BooksException {
		log.debug("Test deleteInventoryNotFound");
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(1);
		OrderItemDTO o = orderItemS.create(req);
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(o.getId());
		req2.setOrderId(o.getOrderId());
		req2.setInventoryId(2323);
		req2.setQuantity(o.getQuantity());
		Assertions.assertThatThrownBy(() -> orderItemS.delete(req2)).isInstanceOf(BooksException.class);
	}

	@Test
	@Order(14)
	public void deleteNotEnoughStock() throws BooksException {
		log.debug("Test deleteNotEnoughStock");
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(1);
		OrderItemDTO o = orderItemS.create(req);
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(o.getId());
		req2.setOrderId(o.getOrderId());
		req2.setInventoryId(o.getInventory());
		req2.setQuantity(555555);
		Assertions.assertThatThrownBy(() -> orderItemS.delete(req2)).isInstanceOf(BooksException.class);
	}

	@Test
	@Order(15)
	public void createOrderNull() throws BooksException {
		log.debug("Test createOrderNull");
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(null);
		req.setInventoryId(1);
		req.setQuantity(1);
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(16)
	public void createQuantityNull() throws BooksException {
		log.debug("Test createQuantityNull");
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(null);
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(17)
	public void createNotEnoughCopy() throws BooksException {
		log.debug("Test createNotEnoughCopy");
		OrderItemReq req = new OrderItemReq();
		req.setId(17);
		req.setOrderId(1);
		req.setInventoryId(1);
		req.setQuantity(50000);
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
}
