package com.betacom.books.be.services.implementations;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import javax.swing.text.Utilities;

import org.springframework.stereotype.Service;

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
import com.betacom.books.be.utils.UtilsReview;

import org.springframework.transaction.annotation.Transactional;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class ReviewImpl extends Utilities implements IReviewServices {

	private IReviewRepository reviewR;
	private IBookRepository bookR;
	private IUserRepository userR;
	public ReviewImpl(IReviewRepository reviewR,IUserRepository userR,IBookRepository bookR) {
		this.reviewR = reviewR;
		this.userR = userR;
		this.bookR = bookR;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public ReviewDTO create(ReviewReq req) throws BooksException {
		// TODO Auto-generated method stub
		log.debug("create: " + req);
		Review rev = new Review();

		if (req.getUserId() == null)
			throw new BooksException("Utente obbligatorio");

		if (req.getBookId() == null)
			throw new BooksException("Libro obbligatorio");

		if (req.getRating() == null)
			throw new BooksException("Voto recensione obbligatorio");

		Integer rating = req.getRating();

		if (rating < 1 || rating > 5)
			throw new BooksException("Voto recensione fuori range (1-5)");
		
		Optional<User> u = userR.findById(req.getUserId());
		
		if(u.isEmpty()) {
			throw new BooksException("User non valido");
		}
		
		Optional<Book> b = bookR.findById(req.getBookId());
		if(b.isEmpty()) throw new BooksException("Book non esistente");
		
		rev.setBook(b.get());
		rev.setUser(u.get());
		rev.setRating(req.getRating());
		rev.setTitle(req.getTitle());
		rev.setBody(req.getBody());
		rev.setCreatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());

		reviewR.save(rev);

		return UtilsReview.toDTO(rev);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(ReviewReq req) throws BooksException {
		log.debug("delete : " + req);

		Optional<Review> r = reviewR.findById(req.getId());
		if (r.isEmpty()) {
			throw new BooksException("Recensione non trovata");
		}
		reviewR.delete(r.get());
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(ReviewReq req) throws BooksException {
		log.debug("update : " + req);
		Optional<Review> r = reviewR.findById(req.getId());

		if (r.isEmpty()) {
			throw new BooksException("Recensione non trovata");
		}

		Review rev = r.get();

		if (req.getRating() != null) {
			Integer rating = req.getRating();
			if (rating < 1 || rating > 5) {
				throw new BooksException("Voto recensione fuori range (1-5)");
			}
			rev.setRating(rating);
		}

		if (req.getTitle() != null) {
			String title = req.getTitle().trim();
			if (title.isEmpty()) {
				throw new BooksException("Il titolo non può essere vuoto");
			}
			rev.setTitle(title);
		}

		if (req.getBody() != null) {
			rev.setBody(req.getBody());
		}

		reviewR.save(rev);

	}

	@Override
	public List<ReviewDTO> getAll() {
		List<Review> lR = reviewR.findAll();
		return UtilsReview.toDTOList(lR);
	}

}
