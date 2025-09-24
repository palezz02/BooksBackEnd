package com.betacom.books.be.services.interfaces;

import java.util.List;

import com.betacom.books.be.dto.SingInDTO;
import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.exception.BooksException;
import com.betacom.books.be.requests.SingInReq;
import com.betacom.books.be.requests.UserReq;

public interface IUserServices {

	UserDTO create(UserReq req) throws BooksException;

	void delete(UserReq req) throws BooksException;

	void update(UserReq req) throws BooksException;

	UserDTO getById(Integer id) throws BooksException;
	
	List<UserDTO> getAll();
	
	SingInDTO signIn(SingInReq req);

}
