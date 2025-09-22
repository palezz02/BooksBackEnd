package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.InventoryDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.InventoryReq;

public interface IInventoryServices {
	InventoryDTO getById(Integer id) throws BooksException;

	InventoryDTO create(InventoryReq req) throws BooksException;

	void update(InventoryReq req) throws BooksException;

	void delete(InventoryReq req) throws BooksException;

	List<InventoryDTO> getAll();
}
