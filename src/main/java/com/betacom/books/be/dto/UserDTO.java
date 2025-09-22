package com.betacom.books.be.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.betacom.books.be.utils.Roles;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Builder
@Getter
@Setter
public class UserDTO {

	private Integer id;
	private String email;
	private String password;
	private String firstName;
	private String lastName;
	private LocalDate birthDate;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Roles role;
	private List<Integer> reviews;
	private List<Integer> addresses;
	private List<Integer> orders;
}
