package com.betacom.books.be.utils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.betacom.books.be.dto.UserDTO;
import com.betacom.books.be.models.Address;
import com.betacom.books.be.models.Order;
import com.betacom.books.be.models.Review;
import com.betacom.books.be.models.User;

public class UtilsUser {

	  public static UserDTO toDTO(User user) {
	        if (user == null) {
	            return null;
	        }

	        return UserDTO.builder()
	                .id(user.getId())
	                .email(user.getEmail())
	                .password(user.getPassword())
	                .firstName(user.getFirstName())
	                .lastName(user.getLastName())
	                .birthDate(user.getBirthDate())
	                .createdAt(user.getCreatedAt())
	                .updatedAt(user.getUpdatedAt())
	                .role(user.getRole())
	                .reviews(user.getReviews() != null ?
	                         user.getReviews().stream().map(Review::getId).collect(Collectors.toList()) :
	                         Collections.emptyList())
	                .addresses(user.getAddresses() != null ? 
	                		user.getAddresses().stream().map(Address::getId).collect(Collectors.toList()) :
                            Collections.emptyList())
	                .orders(user.getOrders() != null ?
	                        user.getOrders().stream().map(Order::getId).collect(Collectors.toList()) :
	                        Collections.emptyList())
	                .build();
	    }
	
	public static List<UserDTO> toDTOList(List<User> users){
		if (users == null)
			return List.of();
		
		return users.stream()
				.map(UtilsUser::toDTO)
				.toList();
	}
}
