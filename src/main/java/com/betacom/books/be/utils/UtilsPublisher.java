package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Publisher;

public class UtilsPublisher {
	public List<PublisherDTO> buildPublisherDTOList(List<Publisher> lista){
		if (lista == null) {
            return Collections.emptyList();
        }
        return lista.stream()
                    .map(UtilsPublisher::buildPublisherDTO)
                    .collect(Collectors.toList());
	}
	
	public static PublisherDTO buildPublisherDTO(Publisher p) {
	    if (p == null) {
	        return null;
	    }
	    List<Book> books = (p.getBooks() != null) ? p.getBooks() : Collections.emptyList();
	    return PublisherDTO.builder()
	            .id(p.getId())
	            .name(p.getName())
	            .description(p.getDescription())
	            .books(books.stream()
	                    .map(Book::getId)
	                    .collect(Collectors.toList()))
	            .build();
	}
}
