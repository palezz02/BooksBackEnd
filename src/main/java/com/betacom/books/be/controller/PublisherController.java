package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.OrderItemDTO;
import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.requests.OrderItemReq;
import com.betacom.books.be.requests.PublisherReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IOrderItemServices;
import com.betacom.books.be.services.interfaces.IPublisherServices;


@RestController
@RequestMapping("/rest/publisher")
public class PublisherController {
	private IPublisherServices publisherS;
	
	public PublisherController(IPublisherServices publisherS) {
		this.publisherS = publisherS;
	}
	
	@GetMapping("/getAll")
	public ResponseList<PublisherDTO> getAll(){
		ResponseList<PublisherDTO> res = new ResponseList<PublisherDTO>();
		try {
			res.setDati(publisherS.getAll());
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@GetMapping("/getById")
	public ResponseObject<PublisherDTO> getById(@RequestParam(required=true) Integer id){
		ResponseObject<PublisherDTO> res = new ResponseObject<PublisherDTO>();
		try {
			res.setDati(publisherS.getById(id));
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/create")
	public ResponseBase create(@RequestBody(required=true) PublisherReq req){
		ResponseBase res = new ResponseBase();
		try {
			publisherS.create(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/update")
	public ResponseBase update(@RequestBody(required=true) PublisherReq req){
		ResponseBase res = new ResponseBase();
		try {
			publisherS.update(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@DeleteMapping("/delete")
	public ResponseBase delete(@RequestBody(required=true) PublisherReq req){
		ResponseBase res = new ResponseBase();
		try {
			publisherS.delete(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
}
