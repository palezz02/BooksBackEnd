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
	
	private OrderDTO createOrder() throws BooksException {
		UserReq usr = new UserReq();
		usr.setBirthDate(LocalDate.of(2005, 11, 26));
		usr.setEmail("test@test.com");
		usr.setFirstName("test");
		usr.setLastName("test");
		usr.setPassword("1234");
		usr.setRole(Roles.ADMIN);
		
		UserDTO user = userI.create(usr);
		
		AddressReq add = new AddressReq();
		add.setCap("asd");
		add.setCity("asd");
		add.setCountry("asd");
		add.setRegion("asd");
		add.setStreet("asd");
		add.setUser(user.getId());
		
		addI.create(add);
		List<AddressDTO> addressL = addI.getAll();
		AddressDTO address = addressL.get(0);
		
		OrderReq o = new OrderReq();
		o.setStatus(Status.PROCESSING);
		o.setTotal(0);
		o.setOrderNumber((int) (Math.random() * 100000));
		o.setUpdatedAt(LocalDate.now());
		o.setShippingAddress(address.getId());
		orderI.create(o);
		
		return orderI.getById(1);
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
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(2);
		
		Assertions.assertThat(orderItemS.create(req)).isExactlyInstanceOf(OrderItemDTO.class);	
	}
	
	@Test
	@Order(2)
	public void createOrderItemExistingIdTest() throws BooksException {
		log.debug("Test createOrderItemExistingIdTest");
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(1);
		OrderItemDTO dto = orderItemS.create(req);
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(dto.getId());
		req2.setOrderId(order.getId());
		req2.setInventoryId(inv.getId());
		req2.setQuantity(3);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req2)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(3)
	public void createOrderItemNullOrderTest() throws BooksException {
		log.debug("Test createOrderItemNullOrderTest");
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(null);
		req.setInventoryId(inv.getId());
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(4)
	public void createOrderItemNullInventoryTest() throws BooksException {
		log.debug("Test createOrderItemNullInventoryTest");
		OrderDTO order = createOrder();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(null);
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.create(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(5)
	public void updateQuantityTest() throws BooksException {
		log.debug("Test updateQuantityTest");
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(2);
		Integer id = orderItemS.create(req).getId();
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(id);
		req2.setQuantity(5);
		orderItemS.update(req2);

		OrderItemDTO o = orderItemS.getById(id);
		Assertions.assertThat(o.getQuantity()).isEqualTo(5);
	}
	
	@Test
	@Order(6)
	public void updateUnitPriceTest() throws BooksException {
		log.debug("Test updateUnitPriceTest");
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(2);
		Integer id = orderItemS.create(req).getId();
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(id);
		req2.setQuantity(2);
		orderItemS.update(req2);

		OrderItemDTO o = orderItemS.getById(id);
		Assertions.assertThat(o.getUnitPrice()).isEqualTo(new BigDecimal("20.00"));
	}
	
	@Test
	@Order(7)
	public void updateOrderItemNotFoundTest() throws BooksException {
		log.debug("Test updateOrderItemNotFoundTest");
		OrderItemReq req = new OrderItemReq();
		req.setId(99999);
		req.setQuantity(1);
		
		Assertions.assertThatThrownBy(() -> orderItemS.update(req)).isInstanceOf(BooksException.class);
	}
	
	@Test
	@Order(8)
	public void deleteOrderItemTest() throws BooksException {
		log.debug("Test deleteOrderItemTest");
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
		req.setQuantity(1);
		OrderItemDTO o = orderItemS.create(req);
		
		OrderItemReq req2 = new OrderItemReq();
		req2.setId(o.getId());
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
		OrderDTO order = createOrder();
		Inventory inv = createInventory();
		
		OrderItemReq req = new OrderItemReq();
		req.setOrderId(order.getId());
		req.setInventoryId(inv.getId());
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
}
