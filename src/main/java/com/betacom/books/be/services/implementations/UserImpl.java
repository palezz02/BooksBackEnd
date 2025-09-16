package com.betacom.books.be.services.implementations;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.text.Utilities;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.Review;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.services.interfaces.IUserServices;
import com.betacom.books.be.utils.UtilsUser;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class UserImpl extends Utilities implements IUserServices {

	private IUserRepository userR;

	public UserImpl(IUserRepository userR) {
		this.userR = userR;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public UserDTO create(UserReq req) throws BooksException {
		log.debug("create: " + req);

		Objects.requireNonNull(req, "Richiesta User non presente");

		User user = new User();

		String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		String email = req.getEmail();
		if (email == null || email.isBlank())
			throw new BooksException("Email non presente");
		if (!email.matches(EMAIL_REGEX))
			throw new BooksException("Formato email non valido: mail@example.com");
		if (userR.existsByEmail(email))
			throw new BooksException("Mail già presente");

		String pwd = req.getPassword();
		if (pwd == null || pwd.isBlank())
			throw new BooksException("Password non presente");

		String firstName = req.getFirstName();
		if (firstName == null || firstName.isBlank())
			throw new BooksException("Nome utente non presente");

		String lastName = req.getLastName();
		if (lastName == null || lastName.isBlank())
			throw new BooksException("Cognome utente non presente");

		if (req.getBirthDate() != null) {
			if (req.getBirthDate().isAfter(LocalDate.now()))
				throw new BooksException("Data di nascita inserita non valida");
		}

		if (req.getRole() == null)
			throw new BooksException("Ruolo inserito non presente");

		user.setEmail(email.trim());
		user.setPassword(pwd);
		user.setFirstName(firstName.trim());
		user.setLastName(lastName.trim());
		user.setBirthDate(req.getBirthDate());
		user.setCreatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());
		user.setUpdatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());
		user.setRole(req.getRole());
		user.setReviews(new ArrayList<Review>());
		user.setAddresses(new ArrayList<Address>());
		user.setOrders(new ArrayList<Order>());

		userR.save(user);

		return UtilsUser.toDTO(user);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(UserReq req) throws BooksException {
		log.debug("delete: " + req);

		Objects.requireNonNull(req, "Richiesta User non presente");

		if (req.getId() == null)
			throw new BooksException("ID specificato non valido");

		User toDelete = userR.findById(req.getId())
				.orElseThrow(() -> new BooksException("Utente da eliminare non trovato"));

		userR.delete(toDelete);
		log.debug("delete: utente cancellato id= " + toDelete.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(UserReq req) throws BooksException {
		log.debug("update: " + req);

		Objects.requireNonNull(req, "Richiesta User non presente");

		if (req.getId() == null)
			throw new BooksException("Id utente non presente");

		User user = userR.findById(req.getId()).orElseThrow(() -> new BooksException("Utente non trovato"));

		String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		if (req.getEmail() != null) {
			String newEmail = req.getEmail().trim();
			if (newEmail.isBlank())
				throw new BooksException("Email non valida");
			if (!newEmail.matches(EMAIL_REGEX))
				throw new BooksException("Formato email non valido: mail@example.com");

			if (!newEmail.equals(user.getEmail()) && userR.existsByEmail(newEmail))
				throw new BooksException("Mail già presente");

			user.setEmail(newEmail);
		}

		if (req.getPassword() != null) {
			String pwd = req.getPassword();
			if (pwd.isBlank())
				throw new BooksException("Password non valida");
			user.setPassword(pwd);
		}

		if (req.getFirstName() != null) {
			String firstName = req.getFirstName().trim();
			if (firstName.isBlank())
				throw new BooksException("Nome utente non valido");
			user.setFirstName(firstName);
		}

		if (req.getLastName() != null) {
			String lastName = req.getLastName().trim();
			if (lastName.isBlank())
				throw new BooksException("Cognome utente non valido");
			user.setLastName(lastName);
		}

		if (req.getBirthDate() != null) {
			if (req.getBirthDate().isAfter(LocalDate.now()))
				throw new BooksException("Data di nascita inserita non valida");
			user.setBirthDate(req.getBirthDate());
		}

		if (req.getRole() != null) {
			user.setRole(req.getRole());
		}

		user.setUpdatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());

		userR.save(user);
		log.debug("update: utente aggiornato id=" + user.getId());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public List<UserDTO> getAll() {
		log.debug("getAll");
		List<User> users = userR.findAll();
		if (users == null || users.isEmpty())
			return new ArrayList<>();

		return UtilsUser.toDTOList(users);
	}

}
