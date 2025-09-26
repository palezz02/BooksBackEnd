package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.requests.AuthorReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IAuthorService;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/rest/author")
public class AuthorController {

    private IAuthorService authorS;

    public AuthorController(IAuthorService authorS) {
        this.authorS = authorS;
    }

    @GetMapping("/listAll")
    public ResponseList<AuthorDTO> listAll() {
        ResponseList<AuthorDTO> r = new ResponseList<>();
        try {
            r.setDati(authorS.getAll());
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }


    @PostMapping("/create")
    public ResponseBase create(@RequestBody(required = true) AuthorReq req) {
        ResponseBase r = new ResponseBase();
        try {
            authorS.create(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }

    @DeleteMapping("/delete")
    public ResponseBase delete(@RequestBody(required = true) AuthorReq req) {
        ResponseBase r = new ResponseBase();
        try {
            authorS.delete(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }

    @PutMapping("/update")
    public ResponseBase update(@RequestBody(required = true) AuthorReq req) {
        ResponseBase r = new ResponseBase();
        try {
            authorS.update(req);
            r.setRc(true);
        } catch (Exception e) {
            r.setRc(false);
            r.setMsg(e.getMessage());
        }
        return r;
    }
    
    @GetMapping("getById")
	public ResponseObject<AuthorDTO> getById(@RequestParam(required = true) Integer id) {
	    ResponseObject<AuthorDTO> r = new ResponseObject<>();
	    try {
	        r.setDati(authorS.getById(id));
	        r.setRc(true);
	    } catch (Exception e) {
	        r.setRc(false);
	        r.setMsg(e.getMessage());
	        r.setDati(null);
	    }
	    return r;
	}
}
