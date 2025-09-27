package com.betacom.books.be.utils;

import java.util.Collections;

import com.betacom.books.be.dto.AddressDTO;
import com.betacom.books.be.dto.AuthorDTO;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Author;
import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Order;

public class UtilsAddressAuthor {

    public static AddressDTO buildAddressDTO(Address a) {
        if (a == null) {
            return null;
        }

        return AddressDTO.builder()
                .id(a.getId())
                .street(a.getStreet())
                .city(a.getCity())
                .region(a.getRegion())
                .cap(a.getCap())
                .country(a.getCountry())
                .user(a.getUser() != null ? a.getUser().getId() : null)
                .orders(a.getOrders() != null ?
                        a.getOrders().stream().map(Order::getId).toList() :
                        Collections.emptyList())
                .build();
    }

    public static AuthorDTO buildAuthorDTO(Author a) {
        if (a == null) {
            return null;
        }

        return AuthorDTO.builder()
                .id(a.getId())
                .fullName(a.getFullName())
                .biography(a.getBiography())
                .birthDate(a.getBirthDate())
                .deathDate(a.getDeathDate())
                .coverImageUrl(a.getCoverImageUrl())
                .books(a.getBooks() != null ?
                        a.getBooks().stream().map(Book::getId).toList() :
                        Collections.emptyList())
                .build();
    }

    public static java.util.List<AddressDTO> buildListAddressDTO(java.util.List<Address> lA) {
        if (lA == null) {
			return Collections.emptyList();
		}
        return lA.stream().map(UtilsAddressAuthor::buildAddressDTO).toList();
    }

    public static java.util.List<AuthorDTO> buildListAuthorDTO(java.util.List<Author> lA) {
        if (lA == null) {
			return Collections.emptyList();
		}
        return lA.stream().map(UtilsAddressAuthor::buildAuthorDTO).toList();
    }
}
