package com.betacom.books.be.dto;

import java.util.List;


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
