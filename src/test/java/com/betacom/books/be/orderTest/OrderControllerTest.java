package com.betacom.books.be.orderTest;

import com.betacom.books.be.controller.OrderController;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.services.interfaces.IOrderServices;
import com.betacom.books.be.utils.Status;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private IOrderServices orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderReq mkReq(Integer id) {
        return new OrderReq(id, Status.CANCElLED, 100, 1234, 1, LocalDate.now(), null);
    }

    @Test
    @Order(1)
    void getById_Success() throws Exception {
        when(orderService.getById(100)).thenReturn(OrderDTO.builder().id(100).total(200).build());

        ResponseBase r = orderController.getById(100);

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(orderService, times(1)).getById(100);
    }

    @Test
    @Order(2)
    void getById_Error() throws Exception {
        when(orderService.getById(999)).thenThrow(new BooksException("Not found"));

        ResponseBase r = orderController.getById(999);

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Not found");
        verify(orderService, times(1)).getById(999);
    }

    @Test
    @Order(3)
    void create_Success() throws Exception {
        when(orderService.create(any(OrderReq.class))).thenReturn(OrderDTO.builder().id(200).build());

        ResponseBase r = orderController.create(mkReq(200));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(orderService, times(1)).create(any(OrderReq.class));
    }

    @Test
    @Order(4)
    void create_Error() throws Exception {
        when(orderService.create(any(OrderReq.class))).thenThrow(new BooksException("Create error"));

        ResponseBase r = orderController.create(mkReq(201));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Create error");
        verify(orderService, times(1)).create(any(OrderReq.class));
    }

    @Test
    @Order(5)
    void update_Success() throws Exception {
        doNothing().when(orderService).update(any(OrderReq.class));

        ResponseBase r = orderController.update(mkReq(300));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(orderService, times(1)).update(any(OrderReq.class));
    }

    @Test
    @Order(6)
    void update_Error() throws Exception {
        doThrow(new BooksException("Update error")).when(orderService).update(any(OrderReq.class));

        ResponseBase r = orderController.update(mkReq(301));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Update error");
        verify(orderService, times(1)).update(any(OrderReq.class));
    }

    @Test
    @Order(7)
    void delete_Success() throws Exception {
        doNothing().when(orderService).delete(any(OrderReq.class));

        ResponseBase r = orderController.delete(mkReq(400));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(orderService, times(1)).delete(any(OrderReq.class));
    }

    @Test
    @Order(8)
    void delete_Error() throws Exception {
        doThrow(new BooksException("Delete error")).when(orderService).delete(any(OrderReq.class));

        ResponseBase r = orderController.delete(mkReq(401));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Delete error");
        verify(orderService, times(1)).delete(any(OrderReq.class));
    }
}