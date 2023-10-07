package com.smartcontact.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ConfigWebSecurity extends WebSecurityConfigurerAdapter {

	//making object to use in Daoauthentication
	@Bean  
	public UserDetailsService  getUserDetailsService() {
		return new CustomUserDetailServiceImp();
	}
	
	
	
	//password will be encoded
	@Bean
	public BCryptPasswordEncoder getpasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	

	//setting up dao for database and encoding password
	@Bean
	public DaoAuthenticationProvider getauthenticationProvider() {
	
		DaoAuthenticationProvider daoauthenticationProvider = new DaoAuthenticationProvider();
		daoauthenticationProvider.setUserDetailsService(this.getUserDetailsService());
		daoauthenticationProvider.setPasswordEncoder(getpasswordEncoder());
		return daoauthenticationProvider;
	}


	//custom making to login by taking data from database (To authenticate user by what in memory, session, database.........)
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		//authenticating user from DataBase..........
		auth.authenticationProvider(getauthenticationProvider());
	    
		
	}


	/*
	 * custom making which page to show & which page not to show without authentication. So
	 * Putting security on some pages by giving them role & showing normal 
	 * pages without any security
	 */

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf()
		.disable()
		
		.authorizeRequests()
		.antMatchers("/user/**")
		.hasRole("USER")
		
		.antMatchers("/**").permitAll()
		.anyRequest().authenticated()
		
		.and()
		
		.formLogin()
		.loginPage("/signin")
		.loginProcessingUrl("/doLogin")
		.defaultSuccessUrl("/user/index", true)
		.and()
		.oauth2Login()
		.defaultSuccessUrl("/user/index");
		
		
	}
	
}
