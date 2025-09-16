package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.requests.BookReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
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
	public ResponseBase create(@RequestBody (required = true)  BookReq req) {
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
	public ResponseBase update(@RequestBody (required = true)  BookReq req) {
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

	@PostMapping("delete")
	public ResponseBase delete(@RequestBody (required = true)  BookReq req) {
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
	public ResponseBase getById(@RequestParam (required = true) Integer id) {
		ResponseBase r = new ResponseBase();
		try {
			bookService.getById(id);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@GetMapping("getAll")
	public ResponseList<BookDTO> getAll(){
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
	

}
