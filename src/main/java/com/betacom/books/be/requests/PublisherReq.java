package com.betacom.books.be.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PublisherReq {
	private Integer id;
    private String name;
    private String description;
}
