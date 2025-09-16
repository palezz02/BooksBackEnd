package com.betacom.books.be.dto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class AddressDTO {

    private Integer id;
    private String street;
    private String city;
    private String region;
    private String cap;
    private String country;

    private Integer  user;
    private List<Integer> orders;

}
