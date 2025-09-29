package com.betacom.books.be.configurations;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
        .addFilterBefore(new JwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.authorizeHttpRequests((authorize) -> authorize
			// Requests allowed for unauthenticated users (NONE)
			.requestMatchers("/rest/user/create", "/rest/user/signin").permitAll()
			.requestMatchers(
				// USER
				"/rest/user/create",
				"/rest/user/signin",
				"/rest/user/delete",
				"/rest/user/update",
				"/rest/user/getById",
				"/rest/user/getCartBooks",
				
				"/rest/address/create",
				"/rest/address/update",
				
				"/rest/author/create",
				"/rest/author/delete",
				"/rest/author/update",
				"/rest/author/getById",
				
				"/rest/book/create",
				"/rest/book/update",
				"/rest/book/delete",
				"/rest/book/getById",
				"/rest/book/getBestByReviews/**",
				"/rest/book/getBestByCategory",

				"/rest/category/create",
				"/rest/category/update",
				"/rest/category/delete",
				"/rest/category/getById",
				
				"/rest/inventory/create",
				"/rest/inventory/update",
				"/rest/inventory/delete",
				"/rest/inventory/getById",
				"/rest/inventory/getAll",

				"/rest/order/create",
				"/rest/order/update",
				"/rest/order/delete",
				"/rest/order/getById",

				"/rest/orderitem/create",
				"/rest/orderitem/update",
				"/rest/orderitem/delete",
				"/rest/orderitem/getById",

				"/rest/publisher/create",
				"/rest/publisher/update",
				"/rest/publisher/delete",
				"/rest/publisher/getById",

				"/rest/review/create",
				"/rest/review/update",
				"/rest/review/delete"
			).hasRole("CUSTOMER")
			
			// Requests allowed only for ADMIN role
			.requestMatchers("/rest/**").hasRole("ADMIN")
			
			// All other requests must be authenticated
			.anyRequest().authenticated()
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
	PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
}


