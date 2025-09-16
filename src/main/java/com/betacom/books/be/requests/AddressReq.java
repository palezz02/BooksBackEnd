package com.betacom.books.be.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressReq {

	private Integer id;
    private String street;
    private String city;
    private String region;
    private String cap;
    private String country;

    private Integer user;

}
