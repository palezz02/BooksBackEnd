package com.betacom.books.be.repositories;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.Category;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer>{

	Optional<Category> findByName(String name);
}
