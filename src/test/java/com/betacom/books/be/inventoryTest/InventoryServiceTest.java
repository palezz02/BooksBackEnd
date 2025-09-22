package com.betacom.books.be.inventoryTest;

import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.requests.InventoryReq;
import com.betacom.books.be.services.interfaces.IInventoryServices;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryServiceTest {

    @Autowired
    private IInventoryServices inventoryService;

    @MockBean
    private IInventoryRepository inventoryRepository;

    private Inventory mkInventory(Integer id, Integer stock, BigDecimal price) {
        Inventory i = new Inventory();
        i.setId(id);
        i.setStock(stock);
        i.setPrice(price);
        i.setUpdatedAt(LocalDateTime.now());
        i.setOrderItems(Collections.emptyList());
        return i;
    }

    @Test
    void getById_NotFound_Throws() {
        when(inventoryRepository.findById(999)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.getById(999))
            .isInstanceOf(BooksException.class)
            .hasMessage("Inventory with id: 999 doesn't exist");
    }

    @Test
    void getById_Found_ReturnsDTO() throws BooksException {
        Inventory inv = mkInventory(100, 10, new BigDecimal("25.00"));
        when(inventoryRepository.findById(100)).thenReturn(Optional.of(inv));

        InventoryDTO dto = inventoryService.getById(100);

        Assertions.assertThat(dto).isNotNull();
        Assertions.assertThat(dto.getId()).isEqualTo(100);
        Assertions.assertThat(dto.getStock()).isEqualTo(10);
        Assertions.assertThat(dto.getPrice()).isEqualTo(new BigDecimal("25.00"));
    }

    @Test
    void create_AlreadyExists_Throws() {
        Inventory existing = mkInventory(200, 5, new BigDecimal("10.00"));
        when(inventoryRepository.findById(200)).thenReturn(Optional.of(existing));

        InventoryReq req = new InventoryReq(200, 5, new BigDecimal("15.00"), LocalDateTime.now(), null);

        Assertions.assertThatThrownBy(() -> inventoryService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Inventory with id: 200 already exists");
    }

    @Test
    void create_MissingStock_Throws() {
        InventoryReq req = new InventoryReq(201, null, new BigDecimal("15.00"), LocalDateTime.now(), null);
        when(inventoryRepository.findById(201)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Stock mandatory");
    }

    @Test
    void create_MissingPrice_Throws() {
        InventoryReq req = new InventoryReq(202, 10, null, LocalDateTime.now(), null);
        when(inventoryRepository.findById(202)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Price mandatory");
    }

    @Test
    void create_MissingUpdatedAt_Throws() {
        InventoryReq req = new InventoryReq(203, 10, new BigDecimal("12.00"), null, null);
        when(inventoryRepository.findById(203)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.create(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Order number mandatory");
    }

    @Test
    void create_Ok_SavesInventory() throws BooksException {
        when(inventoryRepository.findById(204)).thenReturn(Optional.empty());
        when(inventoryRepository.save(any(Inventory.class))).thenAnswer(inv -> {
            Inventory i = inv.getArgument(0);
            i.setId(204);
            return i;
        });

        InventoryReq req = new InventoryReq(204, 50, new BigDecimal("100.00"), LocalDateTime.now(), null);

        inventoryService.create(req);

        ArgumentCaptor<Inventory> cap = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(cap.capture());
        Assertions.assertThat(cap.getValue().getStock()).isEqualTo(50);
        Assertions.assertThat(cap.getValue().getPrice()).isEqualByComparingTo("100.00");
        Assertions.assertThat(cap.getValue().getUpdatedAt()).isNotNull();
    }

    @Test
    void update_NotFound_Throws() {
        when(inventoryRepository.findById(300)).thenReturn(Optional.empty());

        InventoryReq req = new InventoryReq(300, 20, new BigDecimal("20.00"), LocalDateTime.now(), null);

        Assertions.assertThatThrownBy(() -> inventoryService.update(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Inventory with id: 300 doesn't exist");
    }

    @Test
    void update_Ok_UpdatesFields() throws BooksException {
        Inventory existing = mkInventory(301, 5, new BigDecimal("50.00"));
        when(inventoryRepository.findById(301)).thenReturn(Optional.of(existing));

        InventoryReq req = new InventoryReq(301, 20, new BigDecimal("75.00"), LocalDateTime.now(), null);

        inventoryService.update(req);

        ArgumentCaptor<Inventory> cap = ArgumentCaptor.forClass(Inventory.class);
        verify(inventoryRepository, times(1)).save(cap.capture());
        Assertions.assertThat(cap.getValue().getStock()).isEqualTo(20);
        Assertions.assertThat(cap.getValue().getPrice()).isEqualByComparingTo("75.00");
    }

    @Test
    void delete_NotFound_Throws() {
        when(inventoryRepository.findById(400)).thenReturn(Optional.empty());

        InventoryReq req = new InventoryReq();
        req.setId(400);

        Assertions.assertThatThrownBy(() -> inventoryService.delete(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Inventory with id: 400 doesn't exist");
    }

    @Test
    void delete_HasOrderItems_Throws() {
        Inventory inv = mkInventory(401, 5, new BigDecimal("15.00"));
        OrderItem dummyItem = new OrderItem();
        inv.setOrderItems(Collections.singletonList(dummyItem));
        when(inventoryRepository.findById(401)).thenReturn(Optional.of(inv));

        InventoryReq req = new InventoryReq();
        req.setId(401);

        Assertions.assertThatThrownBy(() -> inventoryService.delete(req))
            .isInstanceOf(BooksException.class)
            .hasMessage("Inventory with id: 401 doesn't have items");
    }

    @Test
    void delete_Ok_NoException() throws BooksException {
        Inventory inv = mkInventory(402, 5, new BigDecimal("15.00"));
        when(inventoryRepository.findById(402)).thenReturn(Optional.of(inv));

        InventoryReq req = new InventoryReq();
        req.setId(402);

        inventoryService.delete(req);
        
        verify(inventoryRepository, never()).delete(any());
    }
}




