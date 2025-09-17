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

	private UserReq buildNewUser(String email) {
		UserReq req = new UserReq();
		req.setEmail(email);
		req.setPassword("password");
		req.setFirstName("Mario");
		req.setLastName("Rossi");
		req.setBirthDate(LocalDate.of(2000, 1, 1));
		return req;
	}

	@Test
	public void createUserImpl() throws BooksException {
		log.debug("test create user");

		UserReq req = buildNewUser("email@esempio.com");

		assertThat(userS.create(req)).isInstanceOf(UserDTO.class);
	}

	@ParameterizedTest
	@CsvSource(value = { "emailprova.com, Formato email non valido: mail@example.com",
			"altraEmail, Formato email non valido: mail@example.com", "'', Email non presente",
			"NULL, Email non presente" }, nullValues = "NULL")
	void createUserImplWithEmailError(String emailValue, String errorMessage) {
		log.debug("test create user with wrong email");
		
		UserReq req = buildNewUser(emailValue);
		
		Assertions.assertThatThrownBy(() -> userS.create(req)).isInstanceOf(BooksException.class)
				.hasMessage(errorMessage);
	}

	@Test
	void createUserImplWithSameEmail() throws BooksException {
		log.debug("test create two user with same email");

		String sameEmail = "email@esempio.com";
		
		UserReq req = buildNewUser(sameEmail);

		userS.create(req);

		UserReq req1 = buildNewUser(sameEmail);

		Assertions.assertThatThrownBy(() -> userS.create(req1)).isInstanceOf(BooksException.class)
				.hasMessage("Mail già presente");
	}

	@ParameterizedTest
	@CsvSource(value = { "NULL,Password non presente", "'',Password non presente",
			"'   ',Password non presente" }, nullValues = "NULL")
	void createUserImplWithPasswordError(String passwordValue, String errorMessage) {
		log.debug("test create user with invalid password");

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
		log.debug("test create with invalid first name");
		
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
		log.debug("test create with invalid last name");
		
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
	@CsvSource({ "2045-09-18", "2030-01-01" })
	void createUserImplWithInvalidBirthDate(String dateStr) {
		log.debug("test create with invalid birth date");
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
