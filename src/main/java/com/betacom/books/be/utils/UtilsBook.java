package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.dto.BookDTO;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Review;

public final class UtilsBook {


	public static BookDTO oldToDTO(Book book) {
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
	            .price(book.getInventory().getPrice())
	            .stock(book.getInventory().getStock())
	            .publisher(book.getPublisher() != null ? book.getPublisher().getId() : null)
	            .authors(book.getAuthors() != null ?
	                     book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()) :
	                     Collections.emptyList())
	            .categories(book.getCategories() != null ?
	                        UtilsCategory.toDTOList(book.getCategories()) :
	                        Collections.emptyList())
	            .reviews(book.getReviews() != null ?
	                     book.getReviews().stream().map(Review::getId).collect(Collectors.toList()) :
	                     Collections.emptyList())
	            .averageRating(
	                    book.getReviews().isEmpty()
	                        ? null
	                        : book.getReviews().stream()
	                              .mapToInt(Review::getRating)
	                              .average()
	                              .orElse(0.0)
	             )
	            .inventoryId(book.getInventory().getId())
	            .build();
	}
	
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
	    	    .price(book.getInventory() != null ? book.getInventory().getPrice() : null)
	    	    .stock(book.getInventory() != null ? book.getInventory().getStock() : null)
	    	    .publisherId(book.getPublisher() != null ? book.getPublisher().getId() : null)
	    	    .publisherName(book.getPublisher() != null ? book.getPublisher().getName() : null)
	    	    .publisherDescription(book.getPublisher() != null ? book.getPublisher().getDescription() : null)
	    	    .authors(book.getAuthors() != null ?
	                     book.getAuthors().stream().map(Author::getId).collect(Collectors.toList()) :
	                     Collections.emptyList())
	    	    .categories(book.getCategories() != null ?
                        UtilsCategory.toDTOList(book.getCategories()) :
                        Collections.emptyList())
	    	    .reviews(book.getReviews() != null
	    	        ? book.getReviews().stream().map(Review::getId).collect(Collectors.toList())
	    	        : Collections.emptyList())
	    	    .averageRating(
	    	        book.getReviews() != null && !book.getReviews().isEmpty()
	    	            ? book.getReviews().stream()
	    	                .mapToInt(Review::getRating)
	    	                .average()
	    	                .orElse(0.0)
	    	            : null
	    	    )
	    	    .inventoryId(book.getInventory() != null ? book.getInventory().getId() : null)
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
