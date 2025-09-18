package com.betacom.books.be.addressTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IAddressRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.AddressReq;
import com.betacom.books.be.services.implementations.AddressImpl;

public class AddressServiceTest {

    @Mock
    private IAddressRepository addressRepository;

    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private AddressImpl addressService;

    private User testUser;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testUser = new User();
        testUser.setId(1);
    }

    @Test
    public void testCreateAddressSuccess() throws BooksException {
        AddressReq req = new AddressReq();
        req.setStreet("Via Roma");
        req.setCity("Milan");
        req.setCap("20100");
        req.setCountry("Italy");
        req.setUser(1);

        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        Address savedAddress = new Address();
        savedAddress.setId(1);
        savedAddress.setUser(testUser);

        when(addressRepository.save(any(Address.class))).thenReturn(savedAddress);

        Integer resultId = addressService.create(req);

        assertEquals(1, resultId);
        verify(addressRepository, times(1)).save(any(Address.class));
    }

    @Test
    public void testCreateAddressUserNotFound() {
        AddressReq req = new AddressReq();
        req.setUser(999);

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            addressService.create(req);
        });

        assertEquals("User not found for id 999", exception.getMessage());
    }

    @Test
    public void testGetAllEmptyDatabase() {
        when(addressRepository.findAll()).thenReturn(Collections.emptyList());

        var addresses = addressService.getAll();

        assertNotNull(addresses);
        assertEquals(0, addresses.size());
    }

    @Test
    public void testDeleteAddressSuccess() throws BooksException {
        Address address = new Address();
        address.setId(1);
        address.setOrders(Collections.emptyList());

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        addressService.delete(new AddressReq(1, null, null, null, null, null, null));

        verify(addressRepository, times(1)).delete(address);
    }

    @Test
    public void testDeleteAddressNotFound() {
        when(addressRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            addressService.delete(new AddressReq(999, null, null, null, null, null, null));
        });

        assertEquals("Address not found with id 999", exception.getMessage());
    }

    @Test
    public void testDeleteAddressWithOrders() {
        Address address = new Address();
        address.setId(1);
        address.setOrders(Collections.singletonList(mock(com.betacom.books.be.models.Order.class)));

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));

        BooksException exception = assertThrows(BooksException.class, () -> {
            addressService.delete(new AddressReq(1, null, null, null, null, null, null));
        });

        assertEquals("Address has associated orders and cannot be deleted", exception.getMessage());
    }

    @Test
    public void testUpdateAddressSuccess() throws BooksException {
        Address address = new Address();
        address.setId(1);
        address.setUser(testUser);

        AddressReq req = new AddressReq();
        req.setId(1);
        req.setStreet("New Street");
        req.setUser(1);

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(addressRepository.save(address)).thenReturn(address);

        addressService.update(req);

        assertEquals("New Street", address.getStreet());
        verify(addressRepository, times(1)).save(address);
    }

    @Test
    public void testUpdateAddressNotFound() {
        AddressReq req = new AddressReq();
        req.setId(999);

        when(addressRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            addressService.update(req);
        });

        assertEquals("Address not found with id 999", exception.getMessage());
    }

    @Test
    public void testUpdateAddressUserNotFound() {
        Address address = new Address();
        address.setId(1);
        address.setUser(testUser);

        AddressReq req = new AddressReq();
        req.setId(1);
        req.setUser(999); // invalid user

        when(addressRepository.findById(1)).thenReturn(Optional.of(address));
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            addressService.update(req);
        });

        assertEquals("User not found for id 999", exception.getMessage());
    }
}
