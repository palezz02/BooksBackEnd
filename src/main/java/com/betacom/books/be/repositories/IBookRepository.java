package com.betacom.books.be.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.betacom.books.be.models.Book;

@Repository
public interface IBookRepository extends JpaRepository<Book, Integer> {

	Optional<Book> findByIsbn(String isbn);

	@Query(value = "SELECT * FROM top_books_per_category(:limit, :offset)", nativeQuery = true)
	List<Book> bestOfCategorys(@Param("limit") Integer limit, @Param("offset") Integer offset);

	@Query(value = "SELECT * FROM top_books_by_rating(:limit, :offset)", nativeQuery = true)
	List<Book> bestOfReviews(@Param("limit") Integer limit, @Param("offset") Integer offset);

	@Query(value = "SELECT * FROM get_books_ordered()", nativeQuery = true)
	List<Book> getBooksOrderedByName();
}
