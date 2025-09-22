package com.betacom.books.be.reviewTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.betacom.books.be.dto.ReviewDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Review;
import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IBookRepository;
import com.betacom.books.be.repositories.IReviewRepository;
import com.betacom.books.be.repositories.IUserRepository;
import com.betacom.books.be.requests.ReviewReq;
import com.betacom.books.be.services.interfaces.IReviewServices;

import lombok.extern.log4j.Log4j2;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewImplTest {

	@Autowired
	private IReviewServices reviewS;

	@MockBean
	private IReviewRepository reviewR;

	@MockBean
	private IUserRepository userR;

	@MockBean
	private IBookRepository bookR;

	// Helpers per entità minime
	private User mkUser(Integer id, String email) {
		User u = new User();
		u.setId(id);
		u.setEmail(email);
		u.setFirstName("Mario");
		u.setLastName("Rossi");
		return u;
	}

	private Book mkBook(Integer id, String title) {
		Book b = new Book();
		b.setId(id);
		b.setTitle(title);
		return b;
	}

	private Review makeReview(Integer id, User u, Book b, Integer rating, String title, String body) {
		Review r = new Review();
		r.setId(id);
		r.setUser(u);
		r.setBook(b);
		r.setRating(rating);
		r.setTitle(title);
		r.setBody(body);
		r.setCreatedAt(LocalDateTime.now());
		return r;
	}

	@Test
	void createMissingUserThrows() {
		log.debug("create: userId mancante");
		ReviewReq req = new ReviewReq();
		req.setBookId(20);
		req.setRating(4);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Utente obbligatorio");
	}

	@Test
	void createMissingBookThrows() {
		log.debug("create: bookId mancante");
		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setRating(4);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Libro obbligatorio");
	}

	@Test
	void createMissingRatingThrows() {
		log.debug("create: rating mancante");
		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Voto recensione obbligatorio");
	}

	@ParameterizedTest
	@CsvSource({ "0", "6", "-1", "100" })
	void createRatingOutOfRangeThrows(Integer badRating) {
		log.debug("create: rating fuori range");
		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);
		req.setRating(badRating);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Voto recensione fuori range (1-5)");
	}

	@Test
	void createWithNonExistingUserThrows() {
		log.debug("create: user inesistente");
		when(userR.findById(10)).thenReturn(Optional.empty());

		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);
		req.setRating(5);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("User non valido");
	}

	@Test
	void createWithNonExistingBookThrows() {
		log.debug("create: book inesistente");
		when(userR.findById(10)).thenReturn(Optional.of(mkUser(10, "mario@esempio.com")));
		when(bookR.findById(20)).thenReturn(Optional.empty());

		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);
		req.setRating(4);

		Assertions.assertThatThrownBy(() -> reviewS.create(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Book non esistente");
	}

	@Test
	void createOk_savesAndMaps() throws BooksException {
		log.debug("create: ok");
		User u = mkUser(10, "mario@esempio.com");
		Book b = mkBook(20, "Il Libro");
		when(userR.findById(10)).thenReturn(Optional.of(u));
		when(bookR.findById(20)).thenReturn(Optional.of(b));
		when(reviewR.save(any(Review.class))).thenAnswer(inv -> {
			Review r = inv.getArgument(0);
			r.setId(100);
			return r;
		});

		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);
		req.setRating(5);
		req.setTitle("Titolo");
		req.setBody("Testo");

		ReviewDTO dto = reviewS.create(req);

		Assertions.assertThat(dto).isInstanceOf(ReviewDTO.class);
		Assertions.assertThat(dto.getRating()).isEqualTo(5);
		Assertions.assertThat(dto.getTitle()).isEqualTo("Titolo");
		Assertions.assertThat(dto.getBody()).isEqualTo("Testo");

		ArgumentCaptor<Review> cap = ArgumentCaptor.forClass(Review.class);
		verify(reviewR, times(1)).save(cap.capture());
		Assertions.assertThat(cap.getValue().getUser()).isEqualTo(u);
		Assertions.assertThat(cap.getValue().getBook()).isEqualTo(b);
		Assertions.assertThat(cap.getValue().getRating()).isEqualTo(5);
		Assertions.assertThat(cap.getValue().getTitle()).isEqualTo("Titolo");
		Assertions.assertThat(cap.getValue().getBody()).isEqualTo("Testo");
		Assertions.assertThat(cap.getValue().getCreatedAt()).isNotNull();
	}

	@Test
	void deleteNotFoundThrows() {
		log.debug("delete: review non trovata");
		when(reviewR.findById(999)).thenReturn(Optional.empty());

		ReviewReq req = new ReviewReq();
		req.setId(999);

		Assertions.assertThatThrownBy(() -> reviewS.delete(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Recensione non trovata");
	}

	@Test
	void deleteOk_deletesByEntity() throws BooksException {
		log.debug("delete: ok");
		Review existing = makeReview(111, mkUser(10, "m@e.com"), mkBook(20, "T"), 4, "A", "B");
		when(reviewR.findById(111)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(111);

		reviewS.delete(req);

		verify(reviewR, times(1)).delete(existing);
	}

	@Test
	void updateNotFoundThrows() {
		log.debug("update: review non trovata");
		when(reviewR.findById(999)).thenReturn(Optional.empty());

		ReviewReq req = new ReviewReq();
		req.setId(999);

		Assertions.assertThatThrownBy(() -> reviewS.update(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Recensione non trovata");
	}

	@ParameterizedTest
	@CsvSource({ "0", "6", "-5" })
	void updateRatingOutOfRangeThrows(Integer badRating) {
		log.debug("update: rating fuori range");
		Review existing = makeReview(200, mkUser(10, "a@a.com"), mkBook(20, "T"), 3, "Old", "Body");
		when(reviewR.findById(200)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(200);
		req.setRating(badRating);

		Assertions.assertThatThrownBy(() -> reviewS.update(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Voto recensione fuori range (1-5)");

		verify(reviewR, never()).save(any(Review.class));
	}

	@Test
	void updateTitleBlankThrows_andDoesNotSave() {
		log.debug("update: title blank");
		Review existing = makeReview(201, mkUser(10, "a@a.com"), mkBook(20, "T"), 3, "Old", "Body");
		when(reviewR.findById(201)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(201);
		req.setTitle("   ");

		Assertions.assertThatThrownBy(() -> reviewS.update(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Il titolo non può essere vuoto");

		verify(reviewR, never()).save(any(Review.class));
	}

	@Test
	void updateRatingOk_savesNewRating() throws BooksException {
		log.debug("update: rating ok");
		Review existing = makeReview(202, mkUser(10, "a@a.com"), mkBook(20, "T"), 2, "Old", "Body");
		when(reviewR.findById(202)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(202);
		req.setRating(5);

		reviewS.update(req);

		ArgumentCaptor<Review> cap = ArgumentCaptor.forClass(Review.class);
		verify(reviewR, times(1)).save(cap.capture());
		Assertions.assertThat(cap.getValue().getRating()).isEqualTo(5);
	}

	@Test
	void updateTitleOk_trimsAndSaves() throws BooksException {
		log.debug("update: title ok con trim");
		Review existing = makeReview(203, mkUser(10, "a@a.com"), mkBook(20, "T"), 3, "Old", "Body");
		when(reviewR.findById(203)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(203);
		req.setTitle("  Nuovo Titolo  ");

		reviewS.update(req);

		ArgumentCaptor<Review> cap = ArgumentCaptor.forClass(Review.class);
		verify(reviewR, times(1)).save(cap.capture());
		Assertions.assertThat(cap.getValue().getTitle()).isEqualTo("Nuovo Titolo");
	}

	@Test
	void updateBodyOk_savesBody() throws BooksException {
		log.debug("update: body ok");
		Review existing = makeReview(204, mkUser(10, "a@a.com"), mkBook(20, "T"), 4, "Old", "Body");
		when(reviewR.findById(204)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(204);
		req.setBody("Corpo aggiornato");

		reviewS.update(req);

		ArgumentCaptor<Review> cap = ArgumentCaptor.forClass(Review.class);
		verify(reviewR, times(1)).save(cap.capture());
		Assertions.assertThat(cap.getValue().getBody()).isEqualTo("Corpo aggiornato");
	}

	@Test
	void updateMixed_validThenInvalid_shouldRollback_noSave() {
		log.debug("update: campo valido poi titolo invalido -> rollback, nessun save");
		Review existing = makeReview(205, mkUser(10, "a@a.com"), mkBook(20, "T"), 2, "Old", "Body");
		when(reviewR.findById(205)).thenReturn(Optional.of(existing));

		ReviewReq req = new ReviewReq();
		req.setId(205);
		req.setRating(4);
		req.setTitle("   ");

		Assertions.assertThatThrownBy(() -> reviewS.update(req))
			.isInstanceOf(BooksException.class)
			.hasMessage("Il titolo non può essere vuoto");

		verify(reviewR, never()).save(any(Review.class));
	}

	@Test
	void getAll_mapsEntitiesToDTOs() {
		log.debug("getAll: mapping entità -> DTO");
		Review r1 = makeReview(300, mkUser(10, "alpha@ex.com"), mkBook(20, "Libro A"), 5, "Titolo A", "Body A");
		Review r2 = makeReview(301, mkUser(11, "beta@ex.com"), mkBook(21, "Libro B"), 3, "Titolo B", "Body B");

		when(reviewR.findAll()).thenReturn(List.of(r1, r2));

		List<ReviewDTO> out = reviewS.getAll();

		Assertions.assertThat(out).isNotNull();
		Assertions.assertThat(out).hasSize(2);
		Assertions.assertThat(out.get(0).getRating()).isEqualTo(5);
		Assertions.assertThat(out.get(0).getTitle()).isEqualTo("Titolo A");
		Assertions.assertThat(out.get(1).getRating()).isEqualTo(3);
		Assertions.assertThat(out.get(1).getTitle()).isEqualTo("Titolo B");
	}
}
