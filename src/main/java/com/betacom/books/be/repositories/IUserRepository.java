package com.betacom.books.be.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	Optional<User> findByUserNameAndPwd(String user, String pwd);

}
