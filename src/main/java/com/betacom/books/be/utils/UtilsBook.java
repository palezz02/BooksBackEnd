package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Review;

public final class UtilsBook {


	    private UtilsBook() {	}

	    public static BookDTO toDTO(Book book) {
	        if (book == null) {
	            return null;
	        }

	        return BookDTO.builder()
	                .id(book.getId())
	                .isbn(book.getIsbn())
	                .title(book.getTitle())
	                .pageCount(book.getPageCount())
	                .description(book.getDescription())
	                .coverImage(book.getCoverImage())
	                .languageCode(book.getLanguageCode())
	                .publicationDate(book.getPublicationDate())
	                .edition(book.getEdition())
	                .publisher(book.getPublisher().getId())
	                .authors(book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()))
	                .categories(UtilsCategory.toDTOList(book.getCategories()))
	                .reviews(book.getReviews().stream().map(Review::getId).collect(Collectors.toList()))
	                .build();
	    }

	    
	    public static List<BookDTO> toDTOList(List<Book> books) {
	        if (books == null) {
	            return Collections.emptyList();
	        }
	        return books.stream()
	                    .map(UtilsBook::toDTO)
	                    .collect(Collectors.toList());
	    }
}
