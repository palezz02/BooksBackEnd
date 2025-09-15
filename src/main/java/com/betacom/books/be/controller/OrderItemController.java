package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

@RestController
@RequestMapping("/rest/orderitem")
public class OrderItemController {
	private IOrderItemServices orderItemS;
	
	public OrderItemController(IOrderItemServices orderItemS) {
		this.orderItemS = orderItemS;
	}
	
	@GetMapping("/getAll")
	public ResponseList<OrderItemDTO> getAll(){
		ResponseList<OrderItemDTO> res = new ResponseList<OrderItemDTO>();
		try {
			res.setDati(orderItemS.getAll());
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@GetMapping("/getById")
	public ResponseObject<OrderItemDTO> getById(@RequestParam(required=true) Integer id){
		ResponseObject<OrderItemDTO> res = new ResponseObject<OrderItemDTO>();
		try {
			res.setDati(orderItemS.getById(id));
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/create")
	public ResponseBase create(@RequestParam(required=true) OrderItemReq req){
		ResponseBase res = new ResponseBase();
		try {
			orderItemS.create(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/update")
	public ResponseBase update(@RequestParam(required=true) OrderItemReq req){
		ResponseBase res = new ResponseBase();
		try {
			orderItemS.update(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
	
	@PostMapping("/delete")
	public ResponseBase delete(@RequestParam(required=true) OrderItemReq req){
		ResponseBase res = new ResponseBase();
		try {
			orderItemS.delete(req);
			res.setRc(true);
		}catch (Exception e) {
			res.setRc(false);
			res.setMsg(e.getMessage());
		}
		return res;
		
	}
}
