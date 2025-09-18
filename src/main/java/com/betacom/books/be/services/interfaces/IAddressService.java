package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.AddressReq;

public interface IAddressService {

	Integer create(AddressReq req) throws BooksException;

	void delete(AddressReq req) throws BooksException;

	void update(AddressReq req) throws BooksException;

	List<AddressDTO> getAll();

}
