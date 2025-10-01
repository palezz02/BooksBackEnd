package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.books.be.models.Author;

public interface IAuthorRepository  extends JpaRepository<Author, Integer> {
	
}
