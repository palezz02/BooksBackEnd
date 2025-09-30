package com.betacom.books.be.configurations;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration {
	@Bean
	SecurityFilterChain filterChain (HttpSecurity http) throws Exception {
		http
		 	.csrf(csrf -> csrf.disable()) 
	        .cors(cors -> {})
			.authorizeHttpRequests((authorize) -> authorize
					.anyRequest().permitAll()
//					.requestMatchers("/rest/user/signin", "/rest/user/create", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
//					.requestMatchers("/rest/address/*", 
//							"/rest/author/listAll", 
//							"/rest/book/getAll", 
//							"/rest/book/getById", 
//							"/rest/category/getAll",
//							"/rest/category/getById",
//							"/rest/order/*", 
//							"/rest/orderitem/*", 
//							"/rest/publisher/getAll", 
//							"/rest/review/*",
//							"/rest/user/getById",
//							"/rest/user/delete",
//							"/rest/user/update",
//							"/rest/publisher/getById").hasAnyRole("ADMIN", "CUSTOMER")
//					.requestMatchers("/rest/**").hasRole("ADMIN")
//					.anyRequest().authenticated()
					);
			return http.build();
	}
	
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200"));

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
}


