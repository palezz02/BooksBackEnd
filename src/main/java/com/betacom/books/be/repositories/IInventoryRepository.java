package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.books.be.models.Inventory;

public interface IInventoryRepository extends JpaRepository<Inventory, Integer>{

}
