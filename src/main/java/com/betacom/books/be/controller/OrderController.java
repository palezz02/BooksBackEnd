package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.requests.OrderReq;
import com.betacom.books.be.response.ResponseObject;

import com.betacom.books.be.services.interfaces.IOrderServices;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("rest/order")
public class OrderController {

	private IOrderServices orderServices;
	
	public OrderController(IOrderServices orderServices) {
		this.orderServices = orderServices;
	}
	
	@GetMapping("/getOrder")
	public ResponseObject<OrderDTO> getOrder(@RequestParam(required = true) Integer id) {
		ResponseObject<OrderDTO> r = new ResponseObject<OrderDTO>();
		try {
			r.setDati(orderServices.getById(id));
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@PostMapping("create")
	public ResponseBase create(@RequestBody (required = true)  OrderReq req) {
		ResponseBase r = new ResponseBase();
		try {
			orderServices.create(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@PostMapping("delete")
	public ResponseBase delete(@RequestBody (required = true)  OrderReq req) {
		ResponseBase r = new ResponseBase();
		try {
			orderServices.delete(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@PutMapping("update")
	public ResponseBase update(@RequestBody (required = true)  OrderReq req) {
		ResponseBase r = new ResponseBase();
		try {
			orderServices.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
}
