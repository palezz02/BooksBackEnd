package com.betacom.books.be.bookTest;

import com.betacom.books.be.controller.BookController;
import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.dto.ReviewBookDTO;
import com.betacom.books.be.requests.BookReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IBookService;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Log4j2
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

	@Mock
	private IBookService bookService;

	@InjectMocks
	private BookController bookController;

	// A reusable method to create a valid BookReq object
	private BookReq createValidBookReq() {
		
		BookReq req = new BookReq();
		req.setIsbn("9780321765723");
		req.setTitle("The Lord of the Rings");
		req.setPageCount(1216);
		req.setDescription("A high fantasy novel by English author J. R. R. Tolkien.");
		req.setCoverImage("https://example.com/lotr-cover.jpg");
		req.setLanguageCode("en");
		req.setPublicationDate(LocalDate.of(1954, 7, 29));
		req.setEdition("First Edition");
		req.setPublisherId(1);
		req.setStock(50);
		req.setPrice(new BigDecimal("25.99"));
		req.setAuthorIds(Arrays.asList(1));
		req.setCategoryIds(Arrays.asList(1));
		return req;
	}

	// A reusable method to create a valid BookDTO object
	private BookDTO createValidBookDTO() {
		return BookDTO.builder().id(1).isbn("9780321765723").title("The Lord of the Rings").pageCount(1216)
				.description("A high fantasy novel by English author J. R. R. Tolkien.")
				.coverImage("https://example.com/lotr-cover.jpg").languageCode("en")
				.publicationDate(LocalDate.of(1954, 7, 29)).edition("First Edition").publisherId(1)
				.authors(new ArrayList<AuthorDTO>()).build();
	}

	// A reusable method to create a different BookReq for updates
	private BookReq createUpdateBookReq(Integer id) {
		BookReq req = new BookReq();
		req.setId(id);
		req.setIsbn("9780743273565");
		req.setTitle("The Great Gatsby");
		req.setPageCount(180);
		req.setDescription("A novel by F. Scott Fitzgerald about the Jazz Age.");
		req.setCoverImage("https://example.com/gatsby-cover.jpg");
		req.setLanguageCode("en");
		req.setPublicationDate(LocalDate.of(1925, 4, 10));
		req.setEdition("Standard Edition");
		req.setPublisherId(2);
		req.setStock(100);
		req.setPrice(new BigDecimal("12.50"));
		req.setAuthorIds(Arrays.asList(1, 2));
		req.setCategoryIds(Arrays.asList(1, 2));
		return req;
	}

	// Reusable method for a different BookDTO
	private BookDTO createUpdateBookDTO() {
		List<AuthorDTO> authorList = new ArrayList<AuthorDTO>();
		authorList.add(mock(AuthorDTO.class));
		return BookDTO.builder().id(1).isbn("9780743273565").title("The Great Gatsby").pageCount(180)
				.description("A novel by F. Scott Fitzgerald about the Jazz Age.")
				.coverImage("https://example.com/gatsby-cover.jpg").languageCode("en")
				.publicationDate(LocalDate.of(1925, 4, 10)).edition("Standard Edition").publisherId(1).authors(authorList)
				.build();
	}

	// region CREATE TESTS
	@Test
	@Order(1)
	public void createBook_Success() throws Exception {
		log.debug("Test Create Book - Success");
		BookReq req = createValidBookReq();
		// doNothing().when(bookService).create(any(BookReq.class));

		ResponseBase r = bookController.create(req);
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getMsg()).isNull();
		verify(bookService, times(1)).create(req);
	}

	@Test
	@Order(2)
	public void createBook_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("Test Create Book - Failure");
		BookReq req = createValidBookReq();
		doThrow(new RuntimeException("Database connection failed")).when(bookService).create(any(BookReq.class));

		ResponseBase r = bookController.create(req);
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("Database connection failed");
		verify(bookService, times(1)).create(req);
	}
	// endregion

	// region UPDATE TESTS
	@Test
	@Order(3)
	public void updateBook_Success() throws Exception {
		log.debug("Test Update Book - Success");
		BookReq req = createUpdateBookReq(1);
		doNothing().when(bookService).update(any(BookReq.class));
		when(bookService.getById(1)).thenReturn(createUpdateBookDTO());

		ResponseBase r = bookController.update(req);
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getMsg()).isNull();

		ResponseObject<BookDTO> s = (ResponseObject<BookDTO>) bookController.getById(1);
		Assertions.assertThat(s.getRc()).isEqualTo(true);
		Assertions.assertThat(s.getDati().getTitle()).isEqualTo(req.getTitle());
		Assertions.assertThat(s.getDati().getIsbn()).isEqualTo(req.getIsbn());
		Assertions.assertThat(s.getDati().getPageCount()).isEqualTo(req.getPageCount());
		verify(bookService, times(1)).update(req);
	}

	@Test
	@Order(4)
	public void updateBook_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("Test Update Book - Failure");
		BookReq req = createUpdateBookReq(999);
		doThrow(new IllegalArgumentException("Book not found for update")).when(bookService).update(any(BookReq.class));

		ResponseBase r = bookController.update(req);
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("Book not found for update");
		verify(bookService, times(1)).update(req);
	}
	// endregion

	// region DELETE TESTS
	@Test
	@Order(5)
	public void deleteBook_Success() throws Exception {
		log.debug("Test Delete Book - Success");
		BookReq req = new BookReq();
		req.setId(1);
		doNothing().when(bookService).delete(any(BookReq.class));

		ResponseBase r = bookController.delete(req);
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getMsg()).isNull();
		verify(bookService, times(1)).delete(req);
	}

	@Test
	@Order(6)
	public void deleteBook_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("Test Delete Book - Failure");
		BookReq req = new BookReq();
		req.setId(999);
		doThrow(new IllegalArgumentException("Book not found")).when(bookService).delete(any(BookReq.class));

		ResponseBase r = bookController.delete(req);
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("Book not found");
		verify(bookService, times(1)).delete(req);
	}
	
	@Test
	@Order(7)
	public void getById_Success_ReturnsBookDTO() throws Exception {
		log.debug("Test Get By Id - Success");
		when(bookService.getById(1)).thenReturn(createValidBookDTO());

		ResponseObject<BookDTO> r = (ResponseObject<BookDTO>) bookController.getById(1);
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getDati()).isNotNull();
		Assertions.assertThat(r.getDati().getId()).isEqualTo(1);
		Assertions.assertThat(r.getDati().getTitle()).isEqualTo("The Lord of the Rings");
		verify(bookService, times(1)).getById(1);
	}

	@Test
	@Order(8)
	public void getById_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("Test Get By Id - Failure");
		when(bookService.getById(999)).thenThrow(new IllegalArgumentException("Book not found"));

		ResponseObject<BookDTO> r = (ResponseObject<BookDTO>) bookController.getById(999);
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("Book not found");
		Assertions.assertThat(r.getDati()).isNull();
		verify(bookService, times(1)).getById(999);
	}

	@Test
	@Order(9)
	public void getAll_Success_ReturnsListOfBooks() throws Exception {
		log.debug("Test Get All - Success");
		List<BookDTO> bookList = Arrays.asList(createValidBookDTO(), createUpdateBookDTO());
		when(bookService.getAll()).thenReturn(bookList);

		ResponseList<BookDTO> r = (ResponseList<BookDTO>) bookController.getAll();
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getDati()).isNotNull();
		Assertions.assertThat(r.getDati()).hasSize(2);
		Assertions.assertThat(r.getDati().get(0).getTitle()).isEqualTo("The Lord of the Rings");
		Assertions.assertThat(r.getDati().get(1).getTitle()).isEqualTo("The Great Gatsby");
		verify(bookService, times(1)).getAll();
	}

	@Test
	@Order(10)
	public void getAll_ServiceError_ReturnsRcFalse() throws Exception {
		log.debug("Test Get All - Failure");
		when(bookService.getAll()).thenThrow(new RuntimeException("Database error"));

		ResponseList<BookDTO> r = (ResponseList<BookDTO>) bookController.getAll();
		Assertions.assertThat(r.getRc()).isEqualTo(false);
		Assertions.assertThat(r.getMsg()).isEqualTo("Database error");
		Assertions.assertThat(r.getDati()).isNullOrEmpty();
		verify(bookService, times(1)).getAll();
	}

	@Test
	@Order(11)
	public void getAll_NoBooksFound_ReturnsEmptyList() throws Exception {
		log.debug("Test Get All - No Books Found");
		when(bookService.getAll()).thenReturn(Collections.emptyList());

		ResponseList<BookDTO> r = (ResponseList<BookDTO>) bookController.getAll();
		Assertions.assertThat(r.getRc()).isEqualTo(true);
		Assertions.assertThat(r.getMsg()).isNull();
		Assertions.assertThat(r.getDati()).isNotNull();
		Assertions.assertThat(r.getDati()).isEmpty();
		verify(bookService, times(1)).getAll();
	}

	@Test
	@Order(13)
	public void getBookReviews_ServiceError_ReturnsRcFalse() throws Exception {
	    log.debug("Test Get Book Reviews - Failure");

	    when(bookService.getBookReviews(999)).thenThrow(new RuntimeException("Reviews not found"));

	    ResponseList<ReviewBookDTO> r = bookController.getBookReviews(999);
	    Assertions.assertThat(r.getRc()).isEqualTo(false);
	    Assertions.assertThat(r.getMsg()).isEqualTo("Reviews not found");
	    Assertions.assertThat(r.getDati()).isNullOrEmpty();
	    verify(bookService, times(1)).getBookReviews(999);
	}

	@Test
	@Order(14)
	public void getBookReviews_NoReviews_ReturnsEmptyList() throws Exception {
	    log.debug("Test Get Book Reviews - Empty");

	    when(bookService.getBookReviews(1)).thenReturn(Collections.emptyList());

	    ResponseList<ReviewBookDTO> r = bookController.getBookReviews(1);
	    Assertions.assertThat(r.getRc()).isEqualTo(true);
	    Assertions.assertThat(r.getDati()).isEmpty();
	    verify(bookService, times(1)).getBookReviews(1);
	}

	@Test
	@Order(15)
	public void getBooksOrderedByName_Success() throws Exception {
	    log.debug("Test Get Books Ordered By Name - Success");

	    List<BookDTO> books = Arrays.asList(createValidBookDTO(), createUpdateBookDTO());
	    when(bookService.getBooksOrderedByName()).thenReturn(books);

	    ResponseList<BookDTO> r = bookController.getBooksOrderedByName();
	    Assertions.assertThat(r.getRc()).isEqualTo(true);
	    Assertions.assertThat(r.getDati()).hasSize(2);
	    Assertions.assertThat(r.getDati().get(0).getTitle()).isEqualTo("The Lord of the Rings");
	    Assertions.assertThat(r.getDati().get(1).getTitle()).isEqualTo("The Great Gatsby");
	    verify(bookService, times(1)).getBooksOrderedByName();
	}

	@Test
	@Order(16)
	public void getBooksOrderedByName_ServiceError() throws Exception {
	    log.debug("Test Get Books Ordered By Name - Failure");

	    when(bookService.getBooksOrderedByName()).thenThrow(new RuntimeException("Sort error"));

	    ResponseList<BookDTO> r = bookController.getBooksOrderedByName();
	    Assertions.assertThat(r.getRc()).isEqualTo(false);
	    Assertions.assertThat(r.getMsg()).isEqualTo("Sort error");
	    Assertions.assertThat(r.getDati()).isNullOrEmpty();
	    verify(bookService, times(1)).getBooksOrderedByName();
	}

	@Test
	@Order(17)
	public void getBooksOrderedByName_NoBooks() throws Exception {
	    log.debug("Test Get Books Ordered By Name - Empty");

	    when(bookService.getBooksOrderedByName()).thenReturn(Collections.emptyList());

	    ResponseList<BookDTO> r = bookController.getBooksOrderedByName();
	    Assertions.assertThat(r.getRc()).isEqualTo(true);
	    Assertions.assertThat(r.getDati()).isEmpty();
	    verify(bookService, times(1)).getBooksOrderedByName();
	}

	// region GET_BEST_BY_CATEGORY TESTS
	@Test
	@Order(18)
	public void getBestByCategory_Success() throws Exception {
	    log.debug("Test Get Best By Category - Success");

	    List<BookDTO> books = Arrays.asList(createValidBookDTO());
	    when(bookService.getBestByCategory(5, 0)).thenReturn(books);

	    ResponseList<BookDTO> r = bookController.getBestByCategory(5, 0);
	    Assertions.assertThat(r.getRc()).isEqualTo(true);
	    Assertions.assertThat(r.getDati()).hasSize(1);
	    Assertions.assertThat(r.getDati().get(0).getTitle()).isEqualTo("The Lord of the Rings");
	    verify(bookService, times(1)).getBestByCategory(5, 0);
	}

	@Test
	@Order(19)
	public void getBestByCategory_ServiceError() throws Exception {
	    log.debug("Test Get Best By Category - Failure");

	    when(bookService.getBestByCategory(5, 0)).thenThrow(new RuntimeException("Category error"));

	    ResponseList<BookDTO> r = bookController.getBestByCategory(5, 0);
	    Assertions.assertThat(r.getRc()).isEqualTo(false);
	    Assertions.assertThat(r.getMsg()).isEqualTo("Category error");
	    Assertions.assertThat(r.getDati()).isNullOrEmpty();
	    verify(bookService, times(1)).getBestByCategory(5, 0);
	}

	@Test
	@Order(20)
	public void getBestByCategory_NoBooks() throws Exception {
	    log.debug("Test Get Best By Category - Empty");

	    when(bookService.getBestByCategory(5, 0)).thenReturn(Collections.emptyList());

	    ResponseList<BookDTO> r = bookController.getBestByCategory(5, 0);
	    Assertions.assertThat(r.getRc()).isEqualTo(true);
	    Assertions.assertThat(r.getDati()).isEmpty();
	    verify(bookService, times(1)).getBestByCategory(5, 0);
	}
	// endregion

}