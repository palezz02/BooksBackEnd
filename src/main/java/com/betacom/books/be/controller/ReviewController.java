package com.betacom.books.be.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.ReviewDTO;
import com.betacom.books.be.requests.ReviewReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.services.interfaces.IReviewServices;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/rest/review")
public class ReviewController {

	private IReviewServices reviewS;
	
	public ReviewController(IReviewServices reviewS) {
		this.reviewS = reviewS;
	}
	
	@PostMapping("create")
	public ResponseBase create(@RequestBody (required = true) ReviewReq req) {
		ResponseBase r = new ResponseBase();
		try {
			reviewS.create(req);
			r.setRc(true);
		} catch (Exception e) {
			// TODO: handle exception
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@DeleteMapping("delete")
	public ResponseBase delete(@RequestBody (required = true) ReviewReq req) {
		ResponseBase r = new ResponseBase();
		try {
			reviewS.delete(req);
			r.setRc(true);
		} catch (Exception e) {
			// TODO: handle exception
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@PutMapping("update")
	public ResponseBase update(@RequestBody (required = true)  ReviewReq req) {
		ResponseBase r = new ResponseBase();
		try {
			reviewS.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@GetMapping("/getAll")
	public ResponseList<ReviewDTO> getAll(){
		ResponseList<ReviewDTO> r = new ResponseList<ReviewDTO>();
		try {
			r.setDati(reviewS.getAll());
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
}
