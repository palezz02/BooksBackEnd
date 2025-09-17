package com.betacom.books.be.userTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.services.interfaces.IUserServices;

import lombok.extern.log4j.Log4j2;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserImplTest {

	@Autowired
	private IUserServices userS;

	@Test
	public void createUserImpl() throws BooksException {
		log.debug("test create user");
		LocalDate birthDate = LocalDate.of(2000, 5, 15);
		UserReq req = new UserReq();
		req.setEmail("email@esempio.com");
		req.setBirthDate(birthDate);
		req.setFirstName("Nome");
		req.setLastName("Cognome");
		req.setPassword("password");

		assertThat(userS.create(req)).isInstanceOf(UserDTO.class);
	}

	@ParameterizedTest
	@CsvSource(value = { "emailprova.com, Formato email non valido: mail@example.com",
			"altraEmail, Formato email non valido: mail@example.com", "'', Email non presente",
			"NULL, Email non presente" }, nullValues = "NULL")
	void createUserImplWithEmailError(String emailValue, String errorMessage) {
		log.debug("test create user with wrong email");
		UserReq req = new UserReq();
		req.setEmail(emailValue);
		req.setBirthDate(null);
		req.setFirstName("Nome");
		req.setLastName("Cognome");
		req.setPassword("password");

		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessage(errorMessage);
	}

	@Test
	void createUserImplWithSameEmail() throws BooksException {
		log.debug("test create user with wrong email");
		UserReq req = new UserReq();
		req.setEmail("email@esempio.com");
		req.setBirthDate(null);
		req.setFirstName("Nome");
		req.setLastName("Cognome");
		req.setPassword("password");

		userS.create(req);

		UserReq req1 = new UserReq();
		req1.setEmail("email@esempio.com");
		req1.setBirthDate(null);
		req1.setFirstName("Nome");
		req1.setLastName("Cognome");
		req1.setPassword("password");
		Assertions.assertThatThrownBy(() -> userS.create(req1)).isInstanceOf(BooksException.class)
				.hasMessage("Mail già presente");
	}

	@ParameterizedTest
	@CsvSource(value = { "NULL,Password non presente", "'',Password non presente",
			"'   ',Password non presente" }, nullValues = "NULL")
	void createUserImplWithPasswordError(String passwordValue, String errorMessage) {
		UserReq req = new UserReq();
		req.setEmail("valid@example.com");
		req.setBirthDate(null);
		req.setFirstName("Nome");
		req.setLastName("Cognome");
		req.setPassword(passwordValue);

		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessageContaining(errorMessage);
	}

	@ParameterizedTest
	@CsvSource(value = { "NULL,Nome utente non presente", "'',Nome utente non presente",
			"'   ',Nome utente non presente" }, nullValues = "NULL")
	void createUserImplWithFirstNameError(String firstNameValue, String errorMessage) {
		UserReq req = new UserReq();
		req.setEmail("valid@example.com");
		req.setBirthDate(null);
		req.setFirstName(firstNameValue);
		req.setLastName("Cognome");
		req.setPassword("password");

		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessageContaining(errorMessage);
	}

	@ParameterizedTest
	@CsvSource(value = { "NULL,Cognome utente non presente", "'',Cognome utente non presente",
			"'   ',Cognome utente non presente" }, nullValues = "NULL")
	void createUserImplWithLastNameError(String lastNameValue, String errorMessage) {
		UserReq req = new UserReq();
		req.setEmail("valid@example.com");
		req.setBirthDate(null);
		req.setFirstName("Nome");
		req.setLastName(lastNameValue);
		req.setPassword("password");

		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessageContaining(errorMessage);
	}

	@ParameterizedTest
	@CsvSource({ "2045-09-18",
			"2030-01-01"
	})
	void createUserImplWithInvalidBirthDate(String dateStr) {
		LocalDate birthDate = LocalDate.parse(dateStr);

		UserReq req = new UserReq();
		req.setEmail("mail@esempio.com");
		req.setFirstName("Mario");
		req.setLastName("Rossi");
		req.setPassword("Password123");
		req.setBirthDate(birthDate);

		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessage("Data di nascita inserita non valida");
	}

}
