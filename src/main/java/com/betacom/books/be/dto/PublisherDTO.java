package com.betacom.books.be.dto;

import java.util.List;

import com.betacom.books.be.models.Book;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class PublisherDTO {
	private Integer id;
    private String name;
    private String description;
	private List<Integer> books;
}
