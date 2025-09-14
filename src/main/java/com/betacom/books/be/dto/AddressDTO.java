package com.betacom.books.be.dto;

import java.util.List;

import com.betacom.books.be.models.User;

public class AddressDTO {

    private Integer id;
    private String street;
    private String city;
    private String region;
    private String cap;
    private String country;

    private User user;
    private List<OrderDTO> orders;

}
