package com.betacom.books.be.configurations;

import java.util.Collections;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.betacom.books.be.models.User;
import com.betacom.books.be.repositories.IUserRepository;

@Configuration
public class CustomUserDetailsService implements UserDetailsService{

	private IUserRepository userRepository;

	public CustomUserDetailsService(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}
	
	   @Override
	    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		   
	        User user = userRepository.findByEmail(email)
	            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

	        return new org.springframework.security.core.userdetails.User(
	            user.getEmail(),
	            "{noop}" + user.getPassword(),
	            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
	        );
	    }
	
	
}
