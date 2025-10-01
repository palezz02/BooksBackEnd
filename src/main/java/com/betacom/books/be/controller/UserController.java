package com.betacom.books.be.controller;


import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.CartBookDTO;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.dto.SingInDTO;
import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.requests.SingInReq;
import com.betacom.books.be.requests.UserReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IUserServices;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/rest/user")
public class UserController {

	private IUserServices userS;

	public UserController(IUserServices userS) {
		this.userS = userS;
	}

	@PostMapping("create")
	public ResponseBase create(@RequestBody(required = true) UserReq req) {
		ResponseBase r = new ResponseBase();
		try {
			userS.create(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@DeleteMapping("delete")
	public ResponseBase delete(@RequestBody(required = true) UserReq req) {
		ResponseBase r = new ResponseBase();
		try {
			userS.delete(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@PutMapping("update")
	public ResponseBase update(@RequestBody(required = true) UserReq req) {
		ResponseBase r = new ResponseBase();
		try {
			userS.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@GetMapping("/getAll")
	public ResponseList<UserDTO> getAll() {
		ResponseList<UserDTO> r = new ResponseList<UserDTO>();
		try {
			r.setDati(userS.getAll());
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	

	@GetMapping("/getById")
	public ResponseObject<UserDTO> getById(@RequestParam(required=true) Integer id){
		ResponseObject<UserDTO> res = new ResponseObject<UserDTO>();
		try {
			res.setDati(userS.getById(id));
			res.setRc(true);
		}catch (Exception e) {
			log.debug("Error: " + e.getMessage());
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/signin")
	public SingInDTO signin(@RequestBody (required = true) SingInReq req) {
		return userS.signIn(req);
	}
	
	@GetMapping("/getCartBooks")
	public ResponseList<CartBookDTO> getCartBooks(@RequestParam(required=true) Integer id){
		ResponseList<CartBookDTO> res = new ResponseList<CartBookDTO>();
		try {
			res.setDati(userS.getCartBooks(id));
			res.setRc(true);
		}catch (Exception e) {
			log.debug("Error: " + e.getMessage());
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}

}
