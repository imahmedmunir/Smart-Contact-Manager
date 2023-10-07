package com.smartcontact.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.smartcontact.dao.UserRepo;
import com.smartcontact.entities.User;

public class CustomUserDetailServiceImp implements UserDetailsService{

	@Autowired
	private UserRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			User userob = repo.getUserByUserName(username); //username is email here 
			 
			if (userob==null) {
				throw new UsernameNotFoundException("User not found !!");
			}
			
			CustomUserDetail customUserDetail = new CustomUserDetail(userob); //use of CustomUserdetail constructor
			
			return customUserDetail;
	}

}
