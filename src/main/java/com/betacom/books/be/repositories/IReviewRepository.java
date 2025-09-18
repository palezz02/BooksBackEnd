package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.Review;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Integer> {

	//TODO complete
}
