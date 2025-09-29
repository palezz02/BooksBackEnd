package com.betacom.books.be.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class SingInDTO {
	private Integer id;
	private Boolean logged;
	private String  role;
	private String token;
}