package com.betacom.books.be.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class BookDTO {

	private Integer id;
    private String isbn;
    private String title;
    private Integer pageCount;
    private String description;
    private String coverImage;
    private String languageCode;
    private LocalDate publicationDate;
    private String edition;
    private Integer stock;
    private BigDecimal price;
    private Integer publisherId;
    private String publisherName;
    private String publisherDescription;
    private List<AuthorDTO> authors;
    private List<CategoryDTO> categories;
    private List<Integer> reviews;
    private Double averageRating;
    private Integer inventoryId;
}
