package com.betacom.books.be.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.PublisherDTO;
import com.betacom.books.be.models.Publisher;

public class UtilsPublisher {
	public List<PublisherDTO> buildPublisherDTOList(List<Publisher> lista){
		return lista.stream().map(p -> PublisherDTO.builder()
				.id(p.getId())
				.name(p.getName())
				.description(p.getDescription())
				.books(p.getBooks())
				.build()).collect(Collectors.toList());
	}
	
	public PublisherDTO buildPublisherDTO(Publisher p){
		return PublisherDTO.builder()
				.id(p.getId())
				.name(p.getName())
				.description(p.getDescription())
				.books(p.getBooks())
				.build();
	}
}
