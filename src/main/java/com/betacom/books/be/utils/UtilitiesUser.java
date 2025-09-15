package com.betacom.books.be.utils;

import java.util.List;

import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.models.User;

public class UtilitiesUser {

	public static UserDTO toDTO(User user) {
		if (user == null) {
			return null;
		}
		return UserDTO.builder().id(user.getId()).email(user.getEmail()).password(user.getPassword())
				.firstName(user.getFirstName()).lastName(user.getLastName()).birthDate(user.getBirthDate())
				.createdAt(user.getCreatedAt()).updatedAt(user.getUpdatedAt()).role(user.getRole())
				.reviews(user.getReviews()).addresses(user.getAddresses()).orders(user.getOrders()).build();
	}
	
	public static List<UserDTO> toDTOList(List<User> users){
		if (users == null)
			return List.of();
		
		return users.stream()
				.map(UtilitiesUser::toDTO)
				.toList();
	}
}
