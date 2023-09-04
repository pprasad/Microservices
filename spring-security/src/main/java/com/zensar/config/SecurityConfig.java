package com.zensar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.zensar.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	// Authentication

	/*
	 * @Bean public UserDetailsManager userDetailsManager(PasswordEncoder
	 * passwordEncoder) { //UserDetails admin =
	 * User.withUsername("tom").password("tom@123").roles("ADMIN").build(); //
	 * UserDetails admin =
	 * User.withUsername("tom").password(passwordEncoder().encode("tom@123")).roles(
	 * "ADMIN").build(); UserDetails admin =
	 * User.withUsername("tom").password(passwordEncoder.encode("tom@123")).roles(
	 * "ADMIN").build(); // UserDetails user =
	 * User.withUsername("jerry").password("jerry@123").roles("USER").build();
	 * UserDetails user =
	 * User.withUsername("jerry").password(passwordEncoder.encode("jerry@123")).
	 * roles("USER").build(); return new InMemoryUserDetailsManager(admin, user); }
	 */

	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsServiceImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		// return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder();
	}

	// Authorization

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		System.out.println("I am inside securityFilterChain");

		http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/all", "/register").permitAll())
				.authorizeHttpRequests(auth -> auth.requestMatchers("/admin","/user").authenticated())
				.formLogin((form) -> form.permitAll());

		return http.build();
	}

	@Bean
	public AuthenticationProvider authenticationProvider() {

		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService());
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());

		return daoAuthenticationProvider;
	}

}