package com.betacom.books.be.bookTest;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Category;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Publisher;
import com.betacom.books.be.models.Review;
import com.betacom.books.be.repositories.IAuthorRepository;
import com.betacom.books.be.repositories.IBookRepository;
import com.betacom.books.be.repositories.ICategoryRepository;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.repositories.IReviewRepository;
import com.betacom.books.be.requests.BookReq;
import com.betacom.books.be.services.implementations.BookImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

	@Mock
	private IBookRepository bookRepository;
	@Mock
	private IPublisherRepository publisherRepository;
	@Mock
	private IAuthorRepository authorRepository;
	@Mock
	private ICategoryRepository categoryRepository;
	@Mock
	private IInventoryRepository inventoryRepository;
	@Mock
	private IReviewRepository iReviewRepository;

	@InjectMocks
	private BookImpl bookService;

	private BookReq validBookReq;
	private Book validBook;
	private Publisher validPublisher;
	private Author validAuthor;
	private Category validCategory;
	private Inventory validInventory;
	private Review validReview;

	@BeforeEach
	public void setUp() {
		validBookReq = new BookReq();
		validBookReq.setIsbn("978-0321765723");
		validBookReq.setTitle("The Lord of the Rings");
		validBookReq.setPageCount(1216);
		validBookReq.setPublisherId(1);
		validBookReq.setAuthorIds(List.of(1));
		validBookReq.setCategoryIds(List.of(1));
		validBookReq.setStock(50);
		validBookReq.setPrice(new BigDecimal("25.99"));

		validPublisher = new Publisher();
		validPublisher.setId(1);

		validAuthor = new Author();
		validAuthor.setId(1);

		validCategory = new Category();
		validCategory.setId(1);

		validInventory = new Inventory();
		validInventory.setId(1);
		validInventory.setStock(50);
		validInventory.setPrice(new BigDecimal("25.99"));

		validReview = new Review();
		validReview.setId(1);

		validBook = new Book();
		validBook.setId(1);
		validBook.setIsbn("978-0321765723");
		validBook.setTitle("The Lord of the Rings");
		validBook.setPageCount(1216);
		validBook.setPublisher(validPublisher);
		validBook.setAuthors(List.of(validAuthor));
		validBook.setCategories(List.of(validCategory));
		validBook.setInventory(validInventory);
		validBook.setReviews(List.of(validReview));
	}

	@Test
	@DisplayName("Create book with valid data should save successfully")
	void create_ValidData_SavesSuccessfully() throws BooksException {
		when(bookRepository.findByIsbn(validBookReq.getIsbn())).thenReturn(Optional.empty());
		when(publisherRepository.findById(validBookReq.getPublisherId())).thenReturn(Optional.of(validPublisher));
		when(authorRepository.findById(anyInt())).thenReturn(Optional.of(validAuthor));
		when(categoryRepository.findById(anyInt())).thenReturn(Optional.of(validCategory));
		when(inventoryRepository.save(any(Inventory.class))).thenReturn(validInventory);
		when(bookRepository.save(any(Book.class))).thenReturn(validBook);

		BookDTO result = bookService.create(validBookReq);

		assertNotNull(result);
		assertEquals(validBook.getTitle(), result.getTitle());
		verify(bookRepository, times(1)).save(any(Book.class));
		verify(inventoryRepository, times(1)).save(any(Inventory.class));
	}

	@Test
	@DisplayName("Create book with null ISBN should throw an exception")
	void create_NullIsbn_ThrowsException() {
		validBookReq.setIsbn(null);

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.create(validBookReq));
		assertEquals("ISBN cannot be null or empty.", thrown.getMessage());
		verify(bookRepository, never()).save(any(Book.class));
	}

	@Test
	@DisplayName("Create book with existing ISBN should throw an exception")
	void create_ExistingIsbn_ThrowsException() {
		when(bookRepository.findByIsbn(validBookReq.getIsbn())).thenReturn(Optional.of(new Book()));

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.create(validBookReq));
		assertEquals("ISBN 978-0321765723 already exist", thrown.getMessage());
		verify(bookRepository, never()).save(any(Book.class));
	}

	@Test
	@DisplayName("Create book with non-existent Publisher ID should throw an exception")
	void create_PublisherNotFound_ThrowsException() {
		when(bookRepository.findByIsbn(validBookReq.getIsbn())).thenReturn(Optional.empty());
		when(publisherRepository.findById(validBookReq.getPublisherId())).thenReturn(Optional.empty());

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.create(validBookReq));
		assertEquals("Id casa editrice non trovato", thrown.getMessage());
	}

	@Test
	@DisplayName("Update book with valid data should save changes")
	void update_ValidData_SavesSuccessfully() throws BooksException {
		validBookReq.setId(1);
		when(bookRepository.findById(1)).thenReturn(Optional.of(validBook));
		when(publisherRepository.findById(validBookReq.getPublisherId())).thenReturn(Optional.of(validPublisher));
		when(authorRepository.findAllById(validBookReq.getAuthorIds())).thenReturn(List.of(validAuthor));
		when(categoryRepository.findAllById(validBookReq.getCategoryIds())).thenReturn(List.of(validCategory));

		bookService.update(validBookReq);

		verify(bookRepository, times(1)).findById(1);
		verify(bookRepository, times(1)).save(any(Book.class));
	}

	@Test
	@DisplayName("Update a non-existent book should throw an exception")
	void update_BookNotFound_ThrowsException() {
		validBookReq.setId(999);
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.update(validBookReq));
		assertEquals("Book not found", thrown.getMessage());
		verify(bookRepository, never()).save(any(Book.class));
	}

	@Test
	@DisplayName("Update with non-existent Author IDs should throw an exception")
	void update_AuthorNotFound_ThrowsException() {
		validBookReq.setId(1);
		validBookReq.setAuthorIds(List.of(1, 999));

		when(bookRepository.findById(1)).thenReturn(Optional.of(validBook));
		when(publisherRepository.findById(validBookReq.getPublisherId())).thenReturn(Optional.of(new Publisher()));
		when(authorRepository.findAllById(validBookReq.getAuthorIds())).thenReturn(List.of(validAuthor));

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.update(validBookReq));

		assertEquals("Authors not found", thrown.getMessage());
	}

	@Test
	@DisplayName("Delete a book with valid ID should remove it from repository")
	void delete_ValidId_DeletesSuccessfully() throws BooksException {
		validBookReq.setId(1);
		when(bookRepository.findById(1)).thenReturn(Optional.of(validBook));
		doNothing().when(bookRepository).delete(validBook);
		doNothing().when(inventoryRepository).delete(validInventory);

		bookService.delete(validBookReq);

		verify(bookRepository, times(1)).delete(validBook);
		verify(inventoryRepository, times(1)).delete(validInventory);
	}

	@Test
	@DisplayName("Delete a non-existent book should throw an exception")
	void delete_BookNotFound_ThrowsException() {
		validBookReq.setId(999);
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.delete(validBookReq));
		assertEquals("Book with ID 999 not found.", thrown.getMessage());
	}

	@Test
	@DisplayName("Delete a book with no associated inventory should throw an exception")
	void delete_NoInventory_ThrowsException() {
		BookReq reqWithId = new BookReq();
		reqWithId.setId(1);

		Book bookWithoutInventory = new Book();
		bookWithoutInventory.setId(1);

		when(bookRepository.findById(1)).thenReturn(Optional.of(bookWithoutInventory));

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.delete(reqWithId));

		assertEquals("Inventory not found", thrown.getMessage());
	}

	@Test
	@DisplayName("Get a book by valid ID should return the correct DTO")
	void getById_ValidId_ReturnsBookDTO() throws BooksException {
		when(bookRepository.findById(1)).thenReturn(Optional.of(validBook));

		BookDTO result = bookService.getById(1);

		assertNotNull(result);
		assertEquals(validBook.getTitle(), result.getTitle());
		assertEquals(validBook.getIsbn(), result.getIsbn());
	}

	@Test
	@DisplayName("Get a book by non-existent ID should throw an exception")
	void getById_NotFound_ThrowsException() {
		when(bookRepository.findById(999)).thenReturn(Optional.empty());

		BooksException thrown = assertThrows(BooksException.class, () -> bookService.getById(999));
		assertEquals("Book with ID 999 not found.", thrown.getMessage());
	}

	@Test
	@DisplayName("Get all books should return a list of BookDTOs")
	void getAll_ReturnsListOfBookDTOs() {
		Book book1 = new Book();
		book1.setId(1);
		book1.setTitle("The Lord of the Rings");
		book1.setPublisher(validPublisher);
		book1.setInventory(validInventory);
		book1.setAuthors(new ArrayList<>());
		book1.setCategories(new ArrayList<>());
		book1.setReviews(new ArrayList<>());

		Book book2 = new Book();
		book2.setId(2);
		book2.setTitle("The Great Gatsby");
		book2.setPublisher(validPublisher);
		book2.setInventory(validInventory);
		book2.setAuthors(new ArrayList<>());
		book2.setCategories(new ArrayList<>());
		book2.setReviews(new ArrayList<>());

		List<Book> allBooks = List.of(book1, book2);
		when(bookRepository.findAll()).thenReturn(allBooks);

		List<BookDTO> result = bookService.getAll();

		assertNotNull(result);
		assertEquals(2, result.size());
		assertEquals("The Lord of the Rings", result.get(0).getTitle());
		assertEquals("The Great Gatsby", result.get(1).getTitle());
	}

	@Test
	@DisplayName("Get all books should return an empty list if no books exist")
	void getAll_NoBooksExist_ReturnsEmptyList() {
		when(bookRepository.findAll()).thenReturn(Collections.emptyList());

		List<BookDTO> result = bookService.getAll();

		assertNotNull(result);
		assertTrue(result.isEmpty());
	}

	@Test
	void getBooksOrderedByName_ReturnsSortedDTOs() {
		Publisher pub = new Publisher();
		pub.setId(1);
		Inventory inv = new Inventory();
		inv.setId(1);
		inv.setStock(10);
		inv.setPrice(new BigDecimal("9.99"));

		Book b1 = new Book();
		b1.setId(1);
		b1.setTitle("A Tale of Two Cities");
		b1.setPublisher(pub);
		b1.setInventory(inv);
		b1.setAuthors(new ArrayList<>());
		b1.setCategories(new ArrayList<>());
		b1.setReviews(new ArrayList<>());

		Book b2 = new Book();
		b2.setId(2);
		b2.setTitle("Moby Dick");
		b2.setPublisher(pub);
		b2.setInventory(inv);
		b2.setAuthors(new ArrayList<>());
		b2.setCategories(new ArrayList<>());
		b2.setReviews(new ArrayList<>());

		Book b3 = new Book();
		b3.setId(3);
		b3.setTitle("Zorba the Greek");
		b3.setPublisher(pub);
		b3.setInventory(inv);
		b3.setAuthors(new ArrayList<>());
		b3.setCategories(new ArrayList<>());
		b3.setReviews(new ArrayList<>());

		when(bookRepository.getBooksOrderedByName()).thenReturn(List.of(b1, b2, b3));

		List<BookDTO> result = bookService.getBooksOrderedByName();

		assertNotNull(result);
		assertEquals(3, result.size());
		assertEquals(List.of("A Tale of Two Cities", "Moby Dick", "Zorba the Greek"),
				result.stream().map(BookDTO::getTitle).toList());
		verify(bookRepository, times(1)).getBooksOrderedByName();
	}

	@Test
	void getBooksOrderedByName_EmptyList() {
		when(bookRepository.getBooksOrderedByName()).thenReturn(Collections.emptyList());

		List<BookDTO> result = bookService.getBooksOrderedByName();

		assertNotNull(result);
		assertTrue(result.isEmpty());
		verify(bookRepository, times(1)).getBooksOrderedByName();
	}
	
	@Test
	void getBestByReviews_ReturnsDTOs() {
	    Publisher pub = new Publisher(); pub.setId(1);
	    Inventory inv = new Inventory(); inv.setId(1); inv.setStock(5); inv.setPrice(new BigDecimal("12.50"));

	    Book b1 = new Book();
	    b1.setId(1);
	    b1.setTitle("Book with Most Reviews");
	    b1.setPublisher(pub);
	    b1.setInventory(inv);
	    b1.setAuthors(new ArrayList<>());
	    b1.setCategories(new ArrayList<>());
	    b1.setReviews(new ArrayList<>());

	    when(bookRepository.bestOfReviews(5, 0)).thenReturn(List.of(b1));

	    List<BookDTO> result = bookService.getBestByReviews(5, 0);

	    assertNotNull(result);
	    assertEquals(1, result.size());
	    assertEquals("Book with Most Reviews", result.get(0).getTitle());
	    verify(bookRepository, times(1)).bestOfReviews(5, 0);
	}

	@Test
	void getBestByReviews_EmptyList() {
	    when(bookRepository.bestOfReviews(5, 0)).thenReturn(Collections.emptyList());

	    List<BookDTO> result = bookService.getBestByReviews(5, 0);

	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	    verify(bookRepository, times(1)).bestOfReviews(5, 0);
	}
	
	@Test
	void getBestByCategory_ReturnsDTOs() {
	    Publisher pub = new Publisher(); pub.setId(1);
	    Inventory inv = new Inventory(); inv.setId(2); inv.setStock(7); inv.setPrice(new BigDecimal("15.00"));

	    Book b1 = new Book();
	    b1.setId(2);
	    b1.setTitle("Best Fantasy Category Book");
	    b1.setPublisher(pub);
	    b1.setInventory(inv);
	    b1.setAuthors(new ArrayList<>());
	    b1.setCategories(new ArrayList<>());
	    b1.setReviews(new ArrayList<>());

	    when(bookRepository.bestOfCategorys(3, 1)).thenReturn(List.of(b1));

	    List<BookDTO> result = bookService.getBestByCategory(3, 1);

	    assertNotNull(result);
	    assertEquals(1, result.size());
	    assertEquals("Best Fantasy Category Book", result.get(0).getTitle());
	    verify(bookRepository, times(1)).bestOfCategorys(3, 1);
	}

	@Test
	void getBestByCategory_EmptyList() {
	    when(bookRepository.bestOfCategorys(3, 1)).thenReturn(Collections.emptyList());

	    List<BookDTO> result = bookService.getBestByCategory(3, 1);

	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	    verify(bookRepository, times(1)).bestOfCategorys(3, 1);
	}


}