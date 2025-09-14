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
	                .publisher(toDTO(book.getPublisher()))
	                .authors(toAuthorDTOList(book.getAuthors()))
	                .categories(toCategoryDTOList(book.getCategories()))
	                .reviews(toReviewDTOList(book.getReviews()))
	                .build();
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
