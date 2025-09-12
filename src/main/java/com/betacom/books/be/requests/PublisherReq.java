package com.betacom.books.be.requests;

import java.math.BigDecimal;
import java.util.List;

import com.betacom.books.be.models.Book;
import com.betacom.books.be.models.Inventory;
import com.betacom.books.be.models.Order;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PublisherReq {
	private Integer id;
    private String name;
    private String description;
}
