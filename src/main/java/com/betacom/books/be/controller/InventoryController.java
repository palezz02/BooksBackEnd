
package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.dto.OrderDTO;
import com.betacom.books.be.requests.InventoryReq;
import com.betacom.books.be.response.ResponseObject;
import com.betacom.books.be.services.interfaces.IInventoryServices;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("rest/inventory")
public class InventoryController {

	private IInventoryServices inventoryServices;

	public InventoryController(IInventoryServices inventoryServices) {
		this.inventoryServices = inventoryServices;
	}

	@GetMapping("/getById")
	public ResponseObject<InventoryDTO> getById(@RequestParam(required = true) Integer id) {
		ResponseObject<InventoryDTO> r = new ResponseObject<InventoryDTO>();
		try {
			r.setDati(inventoryServices.getById(id));
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@PostMapping("create")
	public ResponseBase create(@RequestBody(required = true) InventoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			inventoryServices.create(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@DeleteMapping("delete")
	public ResponseBase delete(@RequestBody(required = true) InventoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			inventoryServices.delete(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@PutMapping("update")
	public ResponseBase update(@RequestBody(required = true) InventoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			inventoryServices.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@GetMapping("getAll")
	public ResponseList<InventoryDTO> getAll(){
		ResponseList<InventoryDTO> r = new ResponseList<InventoryDTO>();
		try {
			r.setDati(inventoryServices.getAll());
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
}
