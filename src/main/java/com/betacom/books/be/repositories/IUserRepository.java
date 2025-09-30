package com.betacom.books.be.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.dto.CartBookDTO;
import com.betacom.books.be.models.User;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

	boolean existsByEmail(String email);

	Optional<User> findByEmailAndPassword(String email, String pwd);
	

	@Query(value = "SELECT * FROM get_cart_books(:p_user_id)", nativeQuery = true)
	List<CartBookDTO> getCartBooks(@Param("p_user_id") Integer userId);
}
