package com.betacom.books.be.userTest;

import java.time.LocalDate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.services.interfaces.IUserServices;
import com.betacom.books.be.utils.Roles;

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
		req.setRole(Roles.ADMIN);
		return req;
	}

	@Test
	@Order(1)
	void getAllShouldNeverReturnNull() {
		log.debug("check getAll consistency");

		Assertions.assertThat(userS.getAll()).isNotNull();

		Assertions.assertThat(userS.getAll()).isInstanceOfAny(java.util.List.class);
	}

	// TODO decidere se fare Mock Injections per il null control

	@Test
	public void createUserImpl() throws BooksException {
		log.debug("test create user");

		UserReq req = buildNewUser("email@esempio.com");

		Assertions.assertThat(userS.create(req)).isInstanceOf(UserDTO.class);
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

		String sameEmail = "prova@esempio.com";

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

	@Test
	void createUserImplWithRoleError() {
		log.debug("test create user with invalid password");
		String errorMessage = "Ruolo inserito non presente";

		UserReq req = new UserReq();
		req.setEmail("valid@example.com");
		req.setRole(null);
		req.setBirthDate(null);
		req.setFirstName("Nome");
		req.setLastName("Cognome");
		req.setPassword("password");

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
		log.debug("test create ");
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

	@Test
	void deleteUserTest() throws BooksException {
		UserReq req = buildNewUser("email@delete.com");
		UserDTO created = userS.create(req);

		Assertions.assertThat(userS.getAll()).extracting(UserDTO::getId).contains(created.getId());

		UserReq del = new UserReq();
		del.setId(created.getId());
		userS.delete(del);

		Assertions.assertThat(userS.getAll()).extracting(UserDTO::getId).doesNotContain(created.getId());
	}

	@Test
	void deleteUserErrorNullIdTest() throws BooksException {

		UserReq del = new UserReq();
		del.setId(null);

		Assertions.assertThatThrownBy(() -> userS.delete(del)).isInstanceOf(BooksException.class)
				.hasMessage("ID specificato non valido");
	}

	@Test
	void deleteUserErrorIdNotFoundTest() throws BooksException {

		UserReq del = new UserReq();
		del.setId(-1);

		Assertions.assertThatThrownBy(() -> userS.delete(del)).isInstanceOf(BooksException.class)
				.hasMessage("Utente da eliminare non trovato");
	}

	@Test
	void updateWithNullRequestThrowsNPE() {
		log.debug("update null request -> NPE");
		Assertions.assertThatThrownBy(() -> userS.update(null)).isInstanceOf(NullPointerException.class)
				.hasMessage("Richiesta User non presente");
	}

	@Test
	void updateWithMissingIdThrows() {
		log.debug("update missing id");
		UserReq req = new UserReq();
		req.setEmail("a@b.com");

		Assertions.assertThatThrownBy(() -> userS.update(req)).isInstanceOf(BooksException.class)
				.hasMessage("Id utente non presente");
	}

	@Test
	void updateWithNotFoundIdThrows() {
		log.debug("update not found id");
		UserReq req = new UserReq();
		req.setId(-1);

		Assertions.assertThatThrownBy(() -> userS.update(req)).isInstanceOf(BooksException.class)
				.hasMessage("Utente non trovato");
	}
	
	@Test
	void updateEmailOk() throws BooksException {
		log.debug("update email ok");
		UserDTO u = userS.create(buildNewUser("update.ok@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setEmail("nuova@mail.com");

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getEmail()).isEqualTo("nuova@mail.com");
	}

	@Test
	void updateEmailBlankThrows() throws BooksException {
		log.debug("update email blank");
		UserDTO u = userS.create(buildNewUser("email.blank@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setEmail("   ");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Email non valida");
	}

	@ParameterizedTest
	@CsvSource({
		"notanemail, emailtest1@prova.com",
		"a@b, emailtest2@prova.com",
		"user@domain,emailtest3@prova.com",
		"user@domain.,emailtest4@prova.com",
		"user@.com,emailtest5@prova.com"
	})
	void updateEmailBadFormatThrows(String bad, String emailTest) throws BooksException {
		log.debug("update email bad format");
		UserDTO u = userS.create(buildNewUser(emailTest));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setEmail(bad);

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class);
	}

	@Test
	void updateEmailDuplicateThrows() throws BooksException {
		log.debug("update email duplicate");
		UserDTO a = userS.create(buildNewUser("dupA@ex.com"));
		UserDTO b = userS.create(buildNewUser("dupB@ex.com"));

		UserReq up = new UserReq();
		up.setId(b.getId());
		up.setEmail("dupA@ex.com");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Mail già presente");

		UserDTO afterB = userS.getAll().stream()
				.filter(x -> x.getId().equals(b.getId()))
				.findFirst().orElseThrow();
		Assertions.assertThat(afterB.getEmail()).isEqualTo("dupB@ex.com");
	}

	@Test
	void updatePasswordBlankThrows() throws BooksException {
		log.debug("update password blank");
		UserDTO u = userS.create(buildNewUser("pwd.blank@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setPassword("  ");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Password non valida");
	}

	@Test
	void updateFirstNameBlankThrows() throws BooksException {
		log.debug("update firstName blank");
		UserDTO u = userS.create(buildNewUser("fn.blank@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setFirstName("   ");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Nome utente non valido");
	}

	@Test
	void updateLastNameBlankThrows() throws BooksException {
		log.debug("update lastName blank");
		UserDTO u = userS.create(buildNewUser("ln.blank@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setLastName("   ");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Cognome utente non valido");
	}

	@Test
	void updateBirthDateFutureThrows() throws BooksException {
		log.debug("update birthDate future");
		UserDTO u = userS.create(buildNewUser("bd.future@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setBirthDate(LocalDate.now().plusDays(1));

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Data di nascita inserita non valida");
	}

	@Test
	void updateRoleOk() throws BooksException {
		log.debug("update role ok");
		UserDTO u = userS.create(buildNewUser("role.ok@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setRole(Roles.ADMIN);

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getRole()).isEqualTo(Roles.ADMIN);
	}

	@Test
	void updateMixedValidAndInvalidFieldsShouldRollback() throws BooksException {
		log.debug("update mixed fields -> rollback");
		UserDTO u = userS.create(buildNewUser("mix.rollback@ex.com"));

		String originalEmail = u.getEmail();

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setEmail("valida@mail.com");
		up.setFirstName("   ");

		Assertions.assertThatThrownBy(() -> userS.update(up))
			.isInstanceOf(BooksException.class)
			.hasMessage("Nome utente non valido");

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getEmail()).isEqualTo(originalEmail);
		Assertions.assertThat(after.getFirstName()).isEqualTo("Mario");
	}
	
	@Test
	void updatePasswordOk() throws BooksException {
		log.debug("update password ok");
		UserDTO u = userS.create(buildNewUser("pwd.ok2@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setPassword("NuovaPassword123");

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getId()).isEqualTo(u.getId());
	}

	@Test
	void updateFirstNameOk() throws BooksException {
		log.debug("update firstName ok");
		UserDTO u = userS.create(buildNewUser("fn.ok@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setFirstName("Luigi");

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getFirstName()).isEqualTo("Luigi");
	}

	@Test
	void updateLastNameOk() throws BooksException {
		log.debug("update lastName ok");
		UserDTO u = userS.create(buildNewUser("ln.ok@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setLastName("Bianchi");

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getLastName()).isEqualTo("Bianchi");
	}

	@Test
	void updateBirthDateOk() throws BooksException {
		log.debug("update birthDate ok");
		UserDTO u = userS.create(buildNewUser("bd.ok@ex.com"));

		UserReq up = new UserReq();
		up.setId(u.getId());
		up.setBirthDate(LocalDate.of(1995, 5, 15));

		userS.update(up);

		UserDTO after = userS.getAll().stream()
				.filter(x -> x.getId().equals(u.getId()))
				.findFirst().orElseThrow();

		Assertions.assertThat(after.getBirthDate()).isEqualTo(LocalDate.of(1995, 5, 15));
	}

}
