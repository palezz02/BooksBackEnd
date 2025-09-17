package com.betacom.books.be.requests;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class SingInReq {
	private String user;
	private String pwd;
}
