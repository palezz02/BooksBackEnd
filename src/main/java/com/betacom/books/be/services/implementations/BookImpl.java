package com.betacom.books.be.services.implementations;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Category;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Publisher;
import com.betacom.books.be.repositories.IAuthorRepository;
import com.betacom.books.be.repositories.IBookRepository;
import com.betacom.books.be.repositories.ICategoryRepository;
import com.betacom.books.be.repositories.IInventoryRepository;
import com.betacom.books.be.repositories.IPublisherRepository;
import com.betacom.books.be.requests.BookReq;
import com.betacom.books.be.services.interfaces.IBookService;
import com.betacom.books.be.utils.UtilsBook;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BookImpl implements IBookService {
	private IBookRepository bookRepository;
	private IPublisherRepository publisherRepository;
	private IAuthorRepository authorRepository;
	private ICategoryRepository categoryRepository;
	private IInventoryRepository invenntoryRepository;

	public BookImpl(IBookRepository bookRepository, IPublisherRepository publisherRepository,
			IAuthorRepository authorRepository, ICategoryRepository categoryRepository,
			IInventoryRepository invenntoryRepository) {
		this.bookRepository = bookRepository;
		this.publisherRepository = publisherRepository;
		this.authorRepository = authorRepository;
		this.categoryRepository = categoryRepository;
		this.invenntoryRepository = invenntoryRepository;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public BookDTO create(BookReq req) throws BooksException {
		log.debug("create: ", req);
		if (req.getIsbn() == null || req.getIsbn().isBlank()) {
			throw new BooksException("ISBN cannot be null or empty.");
		}
		if (req.getTitle() == null || req.getTitle().isBlank()) {
			throw new BooksException("Title cannot be null or empty.");
		}
		if (req.getPageCount() == null) {
			throw new BooksException("Page count cannot be null.");
		}
		if (req.getPublisherId() == null) {
			throw new BooksException("Publisher ID cannot be null.");
		}
		Optional<Book> b = bookRepository.findByIsbn(req.getIsbn().trim());
		if (b.isPresent()) {
			throw new BooksException("ISBN " + req.getIsbn() + " already exist");
		}
//		if (b.get().getInventory() == null) {
////			throw new BooksException("Inventory not found");
//			invenntoryRepository.
//		}

		Book book = new Book();
//		Inventory inventory = b.get().getInventory();

//		inventory.setStock(inventory.getStock() + 1);
//		inventory.setUpdatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());

//		if (inventory.getStock() > 1000) {
//			throw new BooksException("Inventory capacity is full");
//		}

		book.setIsbn(req.getIsbn());
		book.setTitle(req.getTitle());
		book.setPageCount(req.getPageCount());
		book.setDescription(req.getDescription());
		book.setCoverImage(req.getCoverImage());
		book.setLanguageCode(req.getLanguageCode());
		book.setPublicationDate(req.getPublicationDate());
		book.setEdition(req.getEdition());
		Publisher pub = publisherRepository.findById(req.getPublisherId()).orElseThrow(() -> new BooksException("Id casa editrice non trovato"));
		book.setPublisher(pub);
		
		if (book.getAuthors() == null) {
			List<Author> authors = new ArrayList<Author>();
			for (Integer i : req.getAuthorIds()) {
				Author a = authorRepository.findById(i).orElseThrow(() -> new BooksException("Id autore non trovato"));
				authors.add(a);
			}
			book.setAuthors(authors);
		}
		
		if ( book.getCategories() == null) {
			List<Category> categories = new ArrayList<Category>();
			for ( Integer i : req.getCategoryIds()) {
				Category cat = categoryRepository.findById(i).orElseThrow(() -> new BooksException("Id categoria non trovata"));
				categories.add(cat);
			}
			book.setCategories(categories);
		}
		
		Inventory i = new Inventory();
		i.setStock(req.getStock());
		i.setPrice(req.getPrice());
		i.setUpdatedAt((OffsetDateTime.now(ZoneOffset.UTC)).toLocalDateTime());
		
		Inventory inv = invenntoryRepository.save(i);
		book.setInventory(inv);
		bookRepository.save(book);

		// invenntoryRepository.save(inventory);

		return UtilsBook.toDTO(book);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void update(BookReq req) throws BooksException {
		log.debug("update: ", req);
		Optional<Book> b = bookRepository.findById(req.getId());
		if (b.isEmpty()) {
			throw new BooksException("Book not found");
		}

		Book book = b.get();
		if (req.getIsbn() != null)
			book.setIsbn(req.getIsbn());
		if (req.getTitle() != null)
			book.setTitle(req.getTitle());
		if (req.getPageCount() != null)
			book.setPageCount(req.getPageCount());
		if (req.getDescription() != null)
			book.setDescription(req.getDescription());
		if (req.getCoverImage() != null)
			book.setCoverImage(req.getCoverImage());
		if (req.getLanguageCode() != null)
			book.setLanguageCode(req.getLanguageCode());
		if (req.getPublicationDate() != null)
			book.setPublicationDate(req.getPublicationDate());
		if (req.getEdition() != null)
			book.setEdition(req.getEdition());

		if (req.getPublisherId() != null) {
			Publisher publisher = publisherRepository.findById(req.getPublisherId())
					.orElseThrow(() -> new BooksException("Publisher not found"));
			book.setPublisher(publisher);
		}

		if (req.getAuthorIds() != null) {
			List<Author> authors = authorRepository.findAllById(req.getAuthorIds());
			if (authors.size() != req.getAuthorIds().size()) {
				throw new BooksException("Authors not found");
			}
			book.setAuthors(authors);
		}

		if (req.getCategoryIds() != null) {
			List<Category> categories = categoryRepository.findAllById(req.getCategoryIds());
			if (categories.size() != req.getCategoryIds().size()) {
				throw new BooksException("Category not found");
			}
			book.setCategories(categories);
		}

		bookRepository.save(book);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void delete(BookReq req) throws BooksException {
		log.debug("delete: ", req);
		Book existingBook = bookRepository.findById(req.getId())
				.orElseThrow(() -> new BooksException("Book with ID " + req.getId() + " not found."));

		if (existingBook.getInventory() == null) {
			throw new BooksException("Inventory not found");
		}

		Inventory inventory = existingBook.getInventory();


		invenntoryRepository.delete(inventory);

		bookRepository.delete(existingBook);

	}

	@Override
	public BookDTO getById(Integer id) throws BooksException {
		log.debug("getById: ", id);
		Book book = bookRepository.findById(id)
				.orElseThrow(() -> new BooksException("Book with ID " + id + " not found."));
		return UtilsBook.toDTO(book);
	}

	@Override
	public List<BookDTO> getAll() {
		log.debug("getAll");
		List<Book> books = bookRepository.findAll();
		return UtilsBook.toDTOList(books);
	}

}
