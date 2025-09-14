package com.betacom.books.be.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.betacom.books.be.models.Address;

public interface IAddressRepository extends JpaRepository<Address, Integer> {

}
