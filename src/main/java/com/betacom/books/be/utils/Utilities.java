package com.betacom.books.be.utils;

import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Author;

public class Utilities {

    public List<AddressDTO> buildListAddressDTO(List<Address> lA) {
        return lA.stream()g
                .map(a -> AddressDTO.builder()
                        .id(a.getId())
                        .street(a.getStreet())
                        .city(a.getCity())
                        .region(a.getRegion())
                        .cap(a.getCap())
                        .country(a.getCountry())
                        .user((a.getUser() == null) ? null : buildUserDTO(a.getUser()))
                        .orders((a.getOrders() == null) ? null : buildOrderDTO(a.getOrders()))
                        .build())
                .collect(Collectors.toList());
    }

    public List<AuthorDTO> buildListAuthorDTO(List<Author> lA) {
        return lA.stream()
                .map(a -> AuthorDTO.builder()
                        .id(a.getId())
                        .fullName(a.getFullName())
                        .biography(a.getBiography())
                        .birthDate(a.getBirthDate())
                        .deathDate(a.getDeathDate())
                        .coverImageUrl(a.getCoverImageUrl())
                        .books((a.getBooks() == null) ? null : buildBookDTO(a.getBooks()))
                        .build())
                .collect(Collectors.toList());
    }

}
