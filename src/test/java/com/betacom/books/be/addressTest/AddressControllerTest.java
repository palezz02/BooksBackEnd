package com.betacom.books.be.addressTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.betacom.books.be.controller.AddressController;
import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.AddressReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IAddressService;

public class AddressControllerTest {

	@Mock
	private IAddressService addressService;

	@InjectMocks
	private AddressController addressController;

	@BeforeEach
	public void setUp() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testListAllEmptyDatabase() {
		when(addressService.getAll()).thenReturn(Collections.emptyList());

		ResponseList<AddressDTO> response = addressController.listAll();

		assertTrue(response.getRc());
		assertNotNull(response.getDati());
		assertEquals(0, response.getDati().size());
	}

	@Test
	public void testCreateAddress() throws Exception {
		AddressReq req = new AddressReq();
		req.setStreet("Via Roma");
		req.setCity("Milan");
		req.setCap("20100");
		req.setCountry("Italy");
		req.setUser(1);

		when(addressService.create(req)).thenReturn(1);

		ResponseBase response = addressController.create(req);

		assertTrue(response.getRc());
		assertNull(response.getMsg());
	}

	@Test
	public void testCreateAddressThrowsException() throws Exception {
		AddressReq req = new AddressReq();
		req.setUser(999);

		when(addressService.create(req)).thenThrow(new RuntimeException("User not found"));

		ResponseBase response = addressController.create(req);

		assertFalse(response.getRc());
		assertEquals("User not found", response.getMsg());
	}

	@Test
	public void testDeleteAddress() throws Exception {
		AddressReq req = new AddressReq();
		req.setId(1);

		doNothing().when(addressService).delete(req);

		ResponseBase response = addressController.delete(req);

		assertTrue(response.getRc());
	}

	@Test
	public void testDeleteAddressThrowsException() throws Exception {
		AddressReq req = new AddressReq();
		req.setId(999);

		doThrow(new RuntimeException("Address not found")).when(addressService).delete(req);

		ResponseBase response = addressController.delete(req);

		assertFalse(response.getRc());
		assertEquals("Address not found", response.getMsg());
	}

	@Test
	public void testUpdateAddress() throws Exception {
		AddressReq req = new AddressReq();
		req.setId(1);
		req.setStreet("New Street");

		doNothing().when(addressService).update(req);

		ResponseBase response = addressController.update(req);

		assertTrue(response.getRc());
	}

	@Test
	public void testUpdateAddressThrowsException() throws Exception {
		AddressReq req = new AddressReq();
		req.setId(999);

		doThrow(new RuntimeException("Address not found")).when(addressService).update(req);

		ResponseBase response = addressController.update(req);

		assertFalse(response.getRc());
		assertEquals("Address not found", response.getMsg());
	}

	 @Test
	    void getById_WhenServiceReturnsAddress_ShouldReturnRcTrueAndData() throws BooksException {
	        IAddressService service = mock(IAddressService.class);
	        AddressController controller = new AddressController(service);

	        AddressDTO address = mock(AddressDTO.class);
	        address.setId(10);
	        address.setStreet("Main Street");

	        when(service.getById(10)).thenReturn(address);

	        ResponseObject<AddressDTO> result = controller.getById(10);

	        assertThat(result.getRc()).isTrue();
	        assertThat(result.getDati()).isEqualTo(address);
	        verify(service).getById(10);
	    }

	    @Test
	    void getById_WhenServiceThrowsException_ShouldReturnRcFalseAndMsg() throws BooksException {
	        IAddressService service = mock(IAddressService.class);
	        AddressController controller = new AddressController(service);

	        when(service.getById(50)).thenThrow(new RuntimeException("Address not found"));

	        ResponseObject<AddressDTO> result = controller.getById(50);

	        assertThat(result.getRc()).isFalse();
	        assertThat(result.getMsg()).isEqualTo("Address not found");
	    }
}
