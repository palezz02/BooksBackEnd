package com.betacom.books.be.reviewTest;

import com.betacom.books.be.controller.ReviewController;
import com.betacom.books.be.dto.ReviewDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.ReviewReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.services.interfaces.IReviewServices;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class ReviewControllerTest {

	@Mock
	private IReviewServices reviewService;

	@InjectMocks
	private ReviewController reviewController;

	private ReviewReq createValidReviewReq() {
		ReviewReq req = new ReviewReq();
		req.setUserId(10);
		req.setBookId(20);
		req.setRating(5);
		req.setTitle("Titolo recensione");
		req.setBody("Testo della recensione");
		return req;
	}

	private ReviewReq createUpdateReviewReq(Integer id) {
		ReviewReq req = new ReviewReq();
		req.setId(id);
		req.setRating(4);
		req.setTitle("Nuovo titolo");
		req.setBody("Nuovo corpo");
		return req;
	}

	@Test
	void create_ReturnsRcTrue() throws BooksException {
		when(reviewService.create(any(ReviewReq.class))).thenReturn(ReviewDTO.builder().build());
		ResponseBase res = reviewController.create(new ReviewReq());
		Assertions.assertThat(res.getRc()).isTrue();
	}

	@Test
	public void createReview_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("POST /rest/review/create - Failure");
		when(reviewService.create(any(ReviewReq.class))).thenThrow(new RuntimeException("Errore creazione review"));

		ResponseBase r = reviewController.create(createValidReviewReq());

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Errore creazione review");
		verify(reviewService, times(1)).create(any(ReviewReq.class));
	}

	@Test
	public void updateReview_Success() throws Exception {
		log.debug("PUT /rest/review/update - Success");
		doNothing().when(reviewService).update(any(ReviewReq.class));

		ResponseBase r = reviewController.update(createUpdateReviewReq(100));

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		verify(reviewService, times(1)).update(any(ReviewReq.class));
	}

	@Test
	public void updateReview_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("PUT /rest/review/update - Failure");
		doThrow(new IllegalArgumentException("Recensione non trovata")).when(reviewService)
				.update(any(ReviewReq.class));

		ResponseBase r = reviewController.update(createUpdateReviewReq(999));

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Recensione non trovata");
		verify(reviewService, times(1)).update(any(ReviewReq.class));
	}

	@Test
	public void deleteReview_Success() throws Exception {
		log.debug("DELETE /rest/review/delete - Success");
		doNothing().when(reviewService).delete(any(ReviewReq.class));

		ReviewReq req = new ReviewReq();
		req.setId(123);

		ResponseBase r = reviewController.delete(req);

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		verify(reviewService, times(1)).delete(any(ReviewReq.class));
	}

	@Test
	public void deleteReview_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("DELETE /rest/review/delete - Failure");
		doThrow(new IllegalArgumentException("Recensione non trovata")).when(reviewService)
				.delete(any(ReviewReq.class));

		ReviewReq req = new ReviewReq();
		req.setId(999);

		ResponseBase r = reviewController.delete(req);

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Recensione non trovata");
		verify(reviewService, times(1)).delete(any(ReviewReq.class));
	}

	@Test
	public void getAll_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("GET /rest/review/getAll - Failure");
		when(reviewService.getAll()).thenThrow(new RuntimeException("Errore DB"));

		ResponseList<ReviewDTO> r = (ResponseList<ReviewDTO>) reviewController.getAll();

		Assertions.assertThat(r.getRc()).isFalse();
		Assertions.assertThat(r.getMsg()).isEqualTo("Errore DB");
		Assertions.assertThat(r.getDati()).isNullOrEmpty();
		verify(reviewService, times(1)).getAll();
	}

	@Test
	public void getAll_NoReviewsFound_ReturnsEmptyList() throws Exception {
		log.debug("GET /rest/review/getAll - Empty");
		when(reviewService.getAll()).thenReturn(Collections.emptyList());

		ResponseList<ReviewDTO> r = (ResponseList<ReviewDTO>) reviewController.getAll();

		Assertions.assertThat(r.getRc()).isTrue();
		Assertions.assertThat(r.getMsg()).isNull();
		Assertions.assertThat(r.getDati()).isNotNull();
		Assertions.assertThat(r.getDati()).isEmpty();
		verify(reviewService, times(1)).getAll();
	}
}
