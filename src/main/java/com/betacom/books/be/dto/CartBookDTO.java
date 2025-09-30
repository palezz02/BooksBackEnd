package com.betacom.books.be.dto;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartBookDTO {
    private Integer orderId;
    private Integer bookId;
    private Integer orderItemId;
    private Integer stock;
    private Integer inventoryId;
    private String title;
    private String description;
    private String coverImage;
    private Date publicationDate; // usa java.sql.Date per compatibilità JDBC
    private String isbn;
    private Integer pageCount;
    private String languageCode;
    private String edition;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal subtotal;
   
}
