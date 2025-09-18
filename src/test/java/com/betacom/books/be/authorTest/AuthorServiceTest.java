package com.betacom.books.be.authorTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.repositories.IAuthorRepository;
import com.betacom.books.be.requests.AuthorReq;
import com.betacom.books.be.services.implementations.AuthorImpl;

public class AuthorServiceTest {

    @Mock
    private IAuthorRepository authorRepository;

    @InjectMocks
    private AuthorImpl authorService;

    private Author testAuthor;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        testAuthor = new Author();
        testAuthor.setId(1);
        testAuthor.setFullName("George Orwell");
    }

    @Test
    public void testCreateAuthorSuccess() throws BooksException {
        AuthorReq req = new AuthorReq();
        req.setFullName("J.K. Rowling");
        req.setBiography("British author");

        Author savedAuthor = new Author();
        savedAuthor.setId(1);
        savedAuthor.setFullName("J.K. Rowling");

        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        Integer resultId = authorService.create(req);

        assertEquals(1, resultId);
        verify(authorRepository, times(1)).save(any(Author.class));
    }

    @Test
    public void testCreateAuthorMissingName() {
        AuthorReq req = new AuthorReq();
        req.setFullName("");

        BooksException exception = assertThrows(BooksException.class, () -> {
            authorService.create(req);
        });

        assertEquals("Full name is required", exception.getMessage());
    }

    @Test
    public void testGetAllEmptyDatabase() {
        when(authorRepository.findAll()).thenReturn(Collections.emptyList());

        var authors = authorService.getAll();

        assertNotNull(authors);
        assertEquals(0, authors.size());
    }

    @Test
    public void testDeleteAuthorSuccess() throws BooksException {
        when(authorRepository.findById(1)).thenReturn(Optional.of(testAuthor));

        authorService.delete(new AuthorReq(1, null, null, null, null, null));

        verify(authorRepository, times(1)).delete(testAuthor);
    }

    @Test
    public void testDeleteAuthorNotFound() {
        when(authorRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            authorService.delete(new AuthorReq(999, null, null, null, null, null));
        });

        assertEquals("Author not found with id 999", exception.getMessage());
    }

    @Test
    public void testDeleteAuthorWithBooks() {
        testAuthor.setBooks(Collections.singletonList(new Book()));
        when(authorRepository.findById(1)).thenReturn(Optional.of(testAuthor));

        BooksException exception = assertThrows(BooksException.class, () -> {
            authorService.delete(new AuthorReq(1, null, null, null, null, null));
        });

        assertEquals("Author has associated books and cannot be deleted", exception.getMessage());
    }

    @Test
    public void testUpdateAuthorSuccess() throws BooksException {
        AuthorReq req = new AuthorReq();
        req.setId(1);
        req.setFullName("Eric Arthur Blair");
        req.setBiography("Updated bio");
        req.setBirthDate(LocalDate.of(1903, 6, 25));

        when(authorRepository.findById(1)).thenReturn(Optional.of(testAuthor));
        when(authorRepository.save(testAuthor)).thenReturn(testAuthor);

        authorService.update(req);

        assertEquals("Eric Arthur Blair", testAuthor.getFullName());
        assertEquals("Updated bio", testAuthor.getBiography());
        assertEquals(LocalDate.of(1903, 6, 25), testAuthor.getBirthDate());
        verify(authorRepository, times(1)).save(testAuthor);
    }

    @Test
    public void testUpdateAuthorNotFound() {
        AuthorReq req = new AuthorReq();
        req.setId(999);

        when(authorRepository.findById(999)).thenReturn(Optional.empty());

        BooksException exception = assertThrows(BooksException.class, () -> {
            authorService.update(req);
        });

        assertEquals("Author not found with id 999", exception.getMessage());
    }
}
