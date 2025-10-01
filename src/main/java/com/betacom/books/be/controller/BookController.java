package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.dto.ReviewBookDTO;
import com.betacom.books.be.requests.BookReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IBookService;

import lombok.extern.log4j.Log4j2;

@RestController
@RequestMapping("/rest/book/")
@Log4j2
public class BookController {

	private IBookService bookService;

	public BookController(IBookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping("create")
	public ResponseBase create(@RequestBody(required = true) BookReq req) {
		log.debug("CREATE: {req}");
		ResponseBase r = new ResponseBase();
		try {
			bookService.create(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@PutMapping("update")
	public ResponseBase update(@RequestBody(required = true) BookReq req) {
		ResponseBase r = new ResponseBase();
		try {
			bookService.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@DeleteMapping("delete")
	public ResponseBase delete(@RequestBody(required = true) BookReq req) {
		ResponseBase r = new ResponseBase();
		try {
			bookService.delete(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@GetMapping("getById")
	public ResponseObject<BookDTO> getById(@RequestParam(required = true) Integer id) {
		ResponseObject<BookDTO> r = new ResponseObject<>();
		try {
			r.setDati(bookService.getById(id));
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
			r.setDati(null);
		}
		return r;
	}

	@GetMapping("getAll")
	public ResponseList<BookDTO> getAll() {
		ResponseList<BookDTO> r = new ResponseList<BookDTO>();
		try {
			r.setDati(bookService.getAll());
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@GetMapping("getBestByReviews")
	public ResponseList<BookDTO> getBestByReviews(@RequestParam(required = true) Integer limit,
			@RequestParam(required = true) Integer offset) {
		ResponseList<BookDTO> r = new ResponseList<BookDTO>();
		try {
			r.setDati(bookService.getBestByReviews(limit, offset));
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@GetMapping("getBestByCategory")
	public ResponseList<BookDTO> getBestByCategory(@RequestParam(required = true) Integer limit,
			@RequestParam(required = true) Integer offset) {
		ResponseList<BookDTO> r = new ResponseList<BookDTO>();
		try {
			r.setDati(bookService.getBestByCategory(limit, offset));
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}


	@GetMapping("getBooksOrderedByName")
	public ResponseList<BookDTO> getBooksOrderedByName() {
		ResponseList<BookDTO> r = new ResponseList<BookDTO>();
		try {
			r.setDati(bookService.getBooksOrderedByName());
	r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
}
	
	@GetMapping("getBookReviews")
	public ResponseList<ReviewBookDTO> getBookReviews(@RequestParam(required = true) Integer bookId){
		ResponseList<ReviewBookDTO> r = new ResponseList<ReviewBookDTO>();
		try {
			r.setDati(bookService.getBookReviews(bookId));

			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

}
