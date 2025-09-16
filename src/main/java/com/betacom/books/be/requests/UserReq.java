package com.betacom.books.be.requests;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.Review;
import com.betacom.books.be.utils.Roles;

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
public class UserReq {
	private Integer id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private Roles role;
}
