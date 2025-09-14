package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.models.Book;

public final class BookUtils {


	    private BookUtils() {	}

	    public static BookDTO toDTO(Book book) {
	        if (book == null) {
	            return null;
	        }

	        BookDTO dto = new BookDTO();
	        dto.setId(book.getId());
	        dto.setIsbn(book.getIsbn());
	        dto.setTitle(book.getTitle());
	        dto.setPageCount(book.getPageCount());
	        dto.setDescription(book.getDescription());
	        dto.setCoverImage(book.getCoverImage());
	        dto.setLanguageCode(book.getLanguageCode());
	        dto.setPublicationDate(book.getPublicationDate());
	        dto.setEdition(book.getEdition());
	        dto.setPublisher(toDTO(book.getPublisher()));
	        dto.setAuthors(toAuthorDTOList(book.getAuthors()));
	        dto.setCategories(toCategoryDTOList(book.getCategories()));
	        dto.setReviews(toReviewDTOList(book.getReviews()));
	        return dto;
	    }


	    public static List<BookDTO> toDTOList(List<Book> books) {
	        if (books == null) {
	            return Collections.emptyList();
	        }
	        return books.stream()
	                    .map(BookUtils::toDTO)
	                    .collect(Collectors.toList());
	    }
}
