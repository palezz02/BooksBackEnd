package com.betacom.books.be.authorTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.betacom.books.be.controller.AuthorController;
import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.AuthorReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IAuthorService;

public class AuthorControllerTest {

    @Mock
    private IAuthorService authorService;

    @InjectMocks
    private AuthorController authorController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAllEmptyDatabase() {
        when(authorService.getAll()).thenReturn(Collections.emptyList());

        ResponseList<AuthorDTO> response = authorController.listAll();

        assertTrue(response.getRc());
        assertNotNull(response.getDati());
        assertEquals(0, response.getDati().size());
    }

    @Test
    public void testCreateAuthor() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setFullName("J.K. Rowling");

        when(authorService.create(req)).thenReturn(1);

        ResponseBase response = authorController.create(req);

        assertTrue(response.getRc());
        assertNull(response.getMsg());
    }

    @Test
    public void testCreateAuthorThrowsException() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setFullName("");

        when(authorService.create(req)).thenThrow(new RuntimeException("Full name is required"));

        ResponseBase response = authorController.create(req);

        assertFalse(response.getRc());
        assertEquals("Full name is required", response.getMsg());
    }

    @Test
    public void testDeleteAuthor() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setId(1);

        doNothing().when(authorService).delete(req);

        ResponseBase response = authorController.delete(req);

        assertTrue(response.getRc());
    }

    @Test
    public void testDeleteAuthorThrowsException() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setId(999);

        doThrow(new RuntimeException("Author not found")).when(authorService).delete(req);

        ResponseBase response = authorController.delete(req);

        assertFalse(response.getRc());
        assertEquals("Author not found", response.getMsg());
    }

    @Test
    public void testUpdateAuthor() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setId(1);
        req.setFullName("George Orwell");

        doNothing().when(authorService).update(req);

        ResponseBase response = authorController.update(req);

        assertTrue(response.getRc());
    }

    @Test
    public void testUpdateAuthorThrowsException() throws Exception {
        AuthorReq req = new AuthorReq();
        req.setId(999);

        doThrow(new RuntimeException("Author not found")).when(authorService).update(req);

        ResponseBase response = authorController.update(req);

        assertFalse(response.getRc());
        assertEquals("Author not found", response.getMsg());
    }
    
    @Test
    void getById_WhenServiceReturnsAuthor_ShouldReturnRcTrueAndData() throws BooksException {
        IAuthorService service = mock(IAuthorService.class);
        AuthorController controller = new AuthorController(service);

        AuthorDTO author = mock(AuthorDTO.class);
        author.setId(7);
        author.setFullName("George Orwell");

        when(service.getById(7)).thenReturn(author);

        ResponseObject<AuthorDTO> result = controller.getById(7);

        assertThat(result.getRc()).isTrue();
        assertThat(result.getDati()).isEqualTo(author);
        verify(service).getById(7);
    }

    @Test
    void getById_WhenServiceThrowsException_ShouldReturnRcFalseAndMsg() throws BooksException {
        IAuthorService service = mock(IAuthorService.class);
        AuthorController controller = new AuthorController(service);

        when(service.getById(99)).thenThrow(new RuntimeException("Author not found"));

        ResponseObject<AuthorDTO> result = controller.getById(99);

        assertThat(result.getRc()).isFalse();
        assertThat(result.getMsg()).isEqualTo("Author not found");
    }
}
