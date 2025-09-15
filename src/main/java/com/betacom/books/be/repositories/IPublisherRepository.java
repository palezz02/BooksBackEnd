package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.OrderItem;
import com.betacom.books.be.models.Publisher;

@Repository
public interface IPublisherRepository extends JpaRepository<Publisher, Integer> {

}
