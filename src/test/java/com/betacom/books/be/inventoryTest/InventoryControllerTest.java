

package com.betacom.books.be.inventoryTest;

import com.betacom.books.be.controller.InventoryController;
import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.InventoryReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.services.interfaces.IInventoryServices;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class InventoryControllerTest {

    @Mock
    private IInventoryServices inventoryService;

    @InjectMocks
    private InventoryController inventoryController;

    private InventoryReq mkReq(Integer id) {
        return new InventoryReq(id, 10, new BigDecimal("20.00"), LocalDateTime.now(), null);
    }

    @Test
    @Order(1)
    void getById_Success() throws Exception {
        when(inventoryService.getById(100)).thenReturn(InventoryDTO.builder().id(100).stock(5).build());

        ResponseBase r = inventoryController.getById(100);

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(inventoryService, times(1)).getById(100);
    }

    @Test
    @Order(2)
    void getById_Error_ReturnsRcFalse() throws Exception {
        when(inventoryService.getById(999)).thenThrow(new BooksException("Inventory not found"));

        ResponseBase r = inventoryController.getById(999);

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Inventory not found");
        verify(inventoryService, times(1)).getById(999);
    }

    @Test
    @Order(3)
    void create_Success() throws Exception {

        ResponseBase r = inventoryController.create(mkReq(200));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(inventoryService, times(1)).create(any(InventoryReq.class));
    }

    @Test
    @Order(4)
    void create_Error_ReturnsRcFalse() throws Exception {
        doThrow(new BooksException("Create error")).when(inventoryService).create(any(InventoryReq.class));

        ResponseBase r = inventoryController.create(mkReq(201));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Create error");
        verify(inventoryService, times(1)).create(any(InventoryReq.class));
    }

    @Test
    @Order(5)
    void update_Success() throws Exception {
        doNothing().when(inventoryService).update(any(InventoryReq.class));

        ResponseBase r = inventoryController.update(mkReq(300));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(inventoryService, times(1)).update(any(InventoryReq.class));
    }

    @Test
    @Order(6)
    void update_Error_ReturnsRcFalse() throws Exception {
        doThrow(new BooksException("Update error")).when(inventoryService).update(any(InventoryReq.class));

        ResponseBase r = inventoryController.update(mkReq(301));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Update error");
        verify(inventoryService, times(1)).update(any(InventoryReq.class));
    }

    @Test
    @Order(7)
    void delete_Success() throws Exception {
        doNothing().when(inventoryService).delete(any(InventoryReq.class));

        ResponseBase r = inventoryController.delete(mkReq(400));

        Assertions.assertThat(r.getRc()).isTrue();
        Assertions.assertThat(r.getMsg()).isNull();
        verify(inventoryService, times(1)).delete(any(InventoryReq.class));
    }

    @Test
    @Order(8)
    void delete_Error_ReturnsRcFalse() throws Exception {
        doThrow(new BooksException("Delete error")).when(inventoryService).delete(any(InventoryReq.class));

        ResponseBase r = inventoryController.delete(mkReq(401));

        Assertions.assertThat(r.getRc()).isFalse();
        Assertions.assertThat(r.getMsg()).isEqualTo("Delete error");
        verify(inventoryService, times(1)).delete(any(InventoryReq.class));
    }
}