package com.betacom.books.be.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.utils.UtilsBook;
import com.betacom.books.be.utils.UtilsUser;
import com.betacom.books.be.utils.UtilsOrder;
public class UtilsAddressAuthor  {

    public static List<AddressDTO> buildListAddressDTO(List<Address> lA) {
        return lA.stream()
                .map(a -> AddressDTO.builder()
                        .id(a.getId())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .region(a.getRegion())
                        .cap(a.getCap())
                        .country(a.getCountry())
                        .user(a.getUser().getId())
                        .orders(a.getOrders().stream().map(Order::getId).collect(Collectors.toList()))
                        .build()).collect(Collectors.toList());
    }

    public static List<AuthorDTO> buildListAuthorDTO(List<Author> lA) {
        return lA.stream()
                .map(a -> AuthorDTO.builder()
                        .id(a.getId())
                        .fullName(a.getFullName())
                        .biography(a.getBiography())
                        .birthDate(a.getBirthDate())
                        .deathDate(a.getDeathDate())
                        .coverImageUrl(a.getCoverImageUrl())
                        .books(a.getBooks().stream().map(Book::getId).collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }
}
