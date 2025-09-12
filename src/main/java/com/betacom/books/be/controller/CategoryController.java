package com.betacom.books.be.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.betacom.books.be.dto.CategoryDTO;
import com.betacom.books.be.requests.CategoryReq;
import com.betacom.books.be.response.ResponseBase;
import com.betacom.books.be.response.ResponseList;
import com.betacom.books.be.services.interfaces.ICategoryService;

@RestController
@RequestMapping("/rest/category")
public class CategoryController {

	private ICategoryService categoryService;

	public CategoryController(ICategoryService categoryService) {
		this.categoryService = categoryService;
	}
	
	@PostMapping("create")
	public ResponseBase create(@RequestBody (required = true)  CategoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			categoryService.create(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@PutMapping("update")
	public ResponseBase update(@RequestBody (required = true)  CategoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			categoryService.update(req);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}

	@PostMapping("delete")
	public ResponseBase delete(@RequestBody (required = true)  CategoryReq req) {
		ResponseBase r = new ResponseBase();
		try {
			categoryService.delete(req);
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
			categoryService.getById(id);
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	@GetMapping("getAll")
	public ResponseList<CategoryDTO> getAll(){
		ResponseList<CategoryDTO> r = new ResponseList<CategoryDTO>();
		try {
			r.setDati(categoryService.getAll());
			r.setRc(true);
		} catch (Exception e) {
			r.setRc(false);
			r.setMsg(e.getMessage());
		}
		return r;
	}
	
	
}
