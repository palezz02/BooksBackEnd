package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.OrderItem;

@Repository
public interface IOrderItemRepository extends JpaRepository<OrderItem, Integer> {

}
