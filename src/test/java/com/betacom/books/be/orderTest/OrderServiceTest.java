package com.betacom.books.be.orderTest;

import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IOrderRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.services.interfaces.IOrderServices;
import com.betacom.books.be.utils.Status;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    @Autowired
    private IOrderServices orderService;

    @MockBean
    private IOrderRepository orderRepository;

    @MockBean
    private IAddressRepository addressRepository;
    
    @MockBean
    private IUserRepository userRepository;

    private Order mkOrder(Integer id) {
        Order o = new Order();
        o.setId(id);
        o.setStatus(Status.CANCElLED);
        o.setTotal(100);
        o.setOrderNumber(999);
        o.setCreatedAt(LocalDate.now().minusDays(1));
        o.setUpdatedAt(LocalDate.now());
        o.setOrderItems(Collections.emptyList());
        User u = new User();
        u.setId(1);
        o.setUser(u);
        return o;
    }

    @Test
    void getById_NotFound_Throws() {
        when(orderRepository.findById(999)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> orderService.getById(999))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order with id: 999 doesn't exist");
    }

    @Test
    void getById_Found_ReturnsDTO() throws BooksException {
        Order o = mkOrder(100);
        when(orderRepository.findById(100)).thenReturn(Optional.of(o));

        OrderDTO dto = orderService.getById(100);

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isEqualTo(100);
        Assertions.assertThat(dto.getTotal()).isEqualTo(100);
    }

    @Test
    void create_AlreadyExists_Throws() {
        when(orderRepository.findById(200)).thenReturn(Optional.of(mkOrder(200)));

        OrderReq req = new OrderReq(200, Status.CANCElLED, 120, 1234, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order with id: 200 already exists");
    }

    @Test
    void create_MissingStatus_Throws() {
        when(orderRepository.findById(201)).thenReturn(Optional.empty());

        OrderReq req = new OrderReq(201, null, 100, 1234, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Status mandatory");
    }

    @Test
    void create_InvalidAddress_Throws() {
        when(orderRepository.findById(202)).thenReturn(Optional.empty());
        when(addressRepository.findById(1)).thenReturn(Optional.empty());

        OrderReq req = new OrderReq(202, Status.CANCElLED, 100, 1234, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("ShippingAddress mandatory");
    }

    @Test
    void create_MissingTotal_Throws() {
        when(orderRepository.findById(203)).thenReturn(Optional.empty());
        when(addressRepository.findById(1)).thenReturn(Optional.of(new Address()));

        OrderReq req = new OrderReq(203, Status.CANCElLED, null, 1234, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Total mandatory");
    }

    @Test
    void create_MissingOrderNumber_Throws() {
        when(orderRepository.findById(204)).thenReturn(Optional.empty());
        when(addressRepository.findById(1)).thenReturn(Optional.of(new Address()));

        OrderReq req = new OrderReq(204, Status.CANCElLED, 100, null, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order number mandatory");
    }

    @Test
    void create_MissingUpdatedAt_Throws() {
        when(orderRepository.findById(205)).thenReturn(Optional.empty());
        when(addressRepository.findById(1)).thenReturn(Optional.of(new Address()));

        OrderReq req = new OrderReq(205, Status.CANCElLED, 100, 1234, 1, null, null);

        Assertions.assertThatThrownBy(() -> orderService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Updated at mandatory");
    }

    @Test
    void create_Ok_SavesAndReturnsDTO() throws BooksException {
        when(orderRepository.findById(206)).thenReturn(Optional.empty());

        // provide address with id = 1
        Address persistedAddress = new Address();
        persistedAddress.setId(1);
        when(addressRepository.findById(1)).thenReturn(Optional.of(persistedAddress));

        // provide user with id = 1
        User persistedUser = new User();
        persistedUser.setId(1);
//        persistedUser.setName("Test User");
        when(userRepository.findById(1)).thenReturn(Optional.of(persistedUser));

        // simulate DB assigning id = 206 on save (this matches your assertion)
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(206); // simulate generated id
            return o;
        });

        OrderReq req = new OrderReq(206, Status.CANCElLED, 150, 4567, 1, LocalDate.now(), 1);

        OrderDTO dto = orderService.create(req);

        // assertions on DTO
        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isEqualTo(206);
        Assertions.assertThat(dto.getTotal()).isEqualTo(150);

        // capture saved order and assert the fields passed to repository
        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(cap.capture());

        Order saved = cap.getValue();
        Assertions.assertThat(saved.getTotal()).isEqualTo(150);
        Assertions.assertThat(saved.getOrderNumber()).isEqualTo(4567);
        Assertions.assertThat(saved.getStatus()).isEqualTo(Status.CANCElLED);
        Assertions.assertThat(saved.getAddress()).isNotNull();
        Assertions.assertThat(saved.getAddress().getId()).isEqualTo(1);
        Assertions.assertThat(saved.getUser()).isNotNull();
        Assertions.assertThat(saved.getUser().getId()).isEqualTo(1);
    }

    @Test
    void update_NotFound_Throws() {
        when(orderRepository.findById(300)).thenReturn(Optional.empty());

        OrderReq req = new OrderReq(300, Status.CANCElLED, 100, 1234, 1, LocalDate.now(), null);

        Assertions.assertThatThrownBy(() -> orderService.update(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order with id: 300 doesn't exist");
    }

    @Test
    void update_Ok_SavesChanges() throws BooksException {
        Order existing = mkOrder(301);
        when(orderRepository.findById(301)).thenReturn(Optional.of(existing));

        OrderReq req = new OrderReq(301, Status.CANCElLED, 200, 5678, 1, LocalDate.now(), null);

        orderService.update(req);

        ArgumentCaptor<Order> cap = ArgumentCaptor.forClass(Order.class);
        verify(orderRepository, times(1)).save(cap.capture());
    }

    @Test
    void delete_NotFound_Throws() {
        when(orderRepository.findById(400)).thenReturn(Optional.empty());

        OrderReq req = new OrderReq();
        req.setId(400);

        Assertions.assertThatThrownBy(() -> orderService.delete(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order with id: 400 doesn't exist");
    }

    @Test
    void delete_WithItems_Throws() {
        Order o = mkOrder(401);
        o.setOrderItems(Collections.singletonList(new OrderItem()));
        when(orderRepository.findById(401)).thenReturn(Optional.of(o));

        OrderReq req = new OrderReq();
        req.setId(401);

        Assertions.assertThatThrownBy(() -> orderService.delete(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order with id: 401 doesn't have items");
    }

    @Test
    void delete_Ok_RemovesOrder() throws BooksException {
        Order o = mkOrder(402);
        when(orderRepository.findById(402)).thenReturn(Optional.of(o));

        OrderReq req = new OrderReq();
        req.setId(402);

        orderService.delete(req);

        verify(orderRepository, times(1)).delete(o);
    }
}