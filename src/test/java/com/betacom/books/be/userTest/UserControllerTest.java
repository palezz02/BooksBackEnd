package com.betacom.books.be.userTest;

import com.betacom.books.be.controller.UserController;
import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IUserServices;
import com.betacom.books.be.utils.Roles;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

	@Mock
	private IUserServices userService;

	@InjectMocks
	private UserController mockedUserController;

	
	// Helpers
	private UserReq createValidUserReq() {
		UserReq req = new UserReq();
		req.setEmail("mario.rossi@example.com");
		req.setPassword("Password123!");
		req.setFirstName("Mario");
		req.setLastName("Rossi");
		req.setBirthDate(LocalDate.of(2000, 1, 1));
		req.setRole(Roles.USER);
		return req;
	}

	private UserReq createUpdateUserReq(Integer id) {
		UserReq req = new UserReq();
		req.setId(id);
		req.setFirstName("Luigi");
		req.setLastName("Bianchi");
		req.setEmail("luigi.bianchi@example.com");
		return req;
	}

	@Test
	public void createUser_Success() throws Exception {
		log.debug("POST /rest/user/create - Success");

		ResponseBase r = mockedUserController.create(createValidUserReq());

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		verify(userService, times(1)).create(any(UserReq.class));
	}

	@Test
	public void createUser_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("POST /rest/user/create - Failure");
		when(userService.create(any(UserReq.class))).thenThrow(new RuntimeException("Errore creazione"));

		ResponseBase r = mockedUserController.create(createValidUserReq());

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Errore creazione");
		verify(userService, times(1)).create(any(UserReq.class));
	}

	@Test
	public void updateUser_Success() throws Exception {
		log.debug("PUT /rest/user/update - Success");
		doNothing().when(userService).update(any(UserReq.class));

		ResponseBase r = mockedUserController.update(createUpdateUserReq(1));

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		verify(userService, times(1)).update(any(UserReq.class));
	}

	@Test
	public void updateUser_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("PUT /rest/user/update - Failure");
		doThrow(new IllegalArgumentException("Id utente non presente")).when(userService).update(any(UserReq.class));

		ResponseBase r = mockedUserController.update(new UserReq()); // id mancante

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Id utente non presente");
		verify(userService, times(1)).update(any(UserReq.class));
	}

	@Test
	public void deleteUser_Success() throws Exception {
		log.debug("DELETE /rest/user/delete - Success");
		doNothing().when(userService).delete(any(UserReq.class));

		UserReq req = new UserReq();
		req.setId(123);

		ResponseBase r = mockedUserController.delete(req);

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		verify(userService, times(1)).delete(any(UserReq.class));
	}

	@Test
	public void deleteUser_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("DELETE /rest/user/delete - Failure");
		doThrow(new IllegalArgumentException("Utente da eliminare non trovato")).when(userService)
				.delete(any(UserReq.class));

		UserReq req = new UserReq();
		req.setId(-1);

		ResponseBase r = mockedUserController.delete(req);

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Utente da eliminare non trovato");
		verify(userService, times(1)).delete(any(UserReq.class));
	}

	@Test
	public void getAll_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("GET /rest/user/getAll - Failure");
		when(userService.getAll()).thenThrow(new RuntimeException("Errore DB"));

		@SuppressWarnings("unchecked")
		ResponseList<UserDTO> r = (ResponseList<UserDTO>) mockedUserController.getAll();

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Errore DB");
		Assertions.assertThat(r.getDati()).isNullOrEmpty();
		verify(userService, times(1)).getAll();
	}

	@Test
	public void getAll_NoUsers_ReturnsEmptyList() throws Exception {
		log.debug("GET /rest/user/getAll - Empty");
		when(userService.getAll()).thenReturn(Collections.emptyList());

		@SuppressWarnings("unchecked")
		ResponseList<UserDTO> r = (ResponseList<UserDTO>) mockedUserController.getAll();

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		Assertions.assertThat(r.getDati()).isNotNull();
		Assertions.assertThat(r.getDati()).isEmpty();
		verify(userService, times(1)).getAll();
	}

	@Test
	public void getById() throws Exception {
		log.debug("User geyById test controller");
		when(userService.getById(any())).thenReturn(mock(UserDTO.class));
		UserController userController = new UserController(userService);
		ResponseObject<UserDTO> r = userController.getById(1);
		
		
		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		Assertions.assertThat(r.getDati()).isNotNull();
	}
	
	@Test
	@Order(1)
	public void getById_Error() throws Exception {
		log.debug("User geyById Error test controller");
		when(userService.getById(any())).thenThrow(BooksException.class);
		UserController userController = new UserController(userService);
		ResponseObject<UserDTO> res = userController.getById(999);
		Assertions.assertThat(res.getRc()).isFalse();
		
	}
	@Test
    void getById_WhenServiceReturnsUser_ShouldReturnRcTrueAndData() throws BooksException {
        IUserServices userService = Mockito.mock(IUserServices.class);
        UserController controller = new UserController(userService);

        UserDTO user = mock(UserDTO.class);
        user.setId(1);
        user.setEmail("test@test.com");

        when(userService.getById(1)).thenReturn(user);

        ResponseObject<UserDTO> result = controller.getById(1);

        assertThat(result.getRc()).isTrue();
        assertThat(result.getDati()).isEqualTo(user);
        verify(userService).getById(1);
    }

    @Test
    void getById_WhenServiceThrowsException_ShouldReturnRcFalseAndMsg() throws BooksException {
        IUserServices userService = Mockito.mock(IUserServices.class);
        UserController controller = new UserController(userService);

        when(userService.getById(99)).thenThrow(new RuntimeException("User not found"));

        ResponseObject<UserDTO> result = controller.getById(99);

        assertThat(result.getRc()).isFalse();
        assertThat(result.getMsg()).isEqualTo("User not found");
    }
}
