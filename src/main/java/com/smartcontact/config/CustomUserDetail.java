package com.smartcontact.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.smartcontact.entities.User;

public class CustomUserDetail implements UserDetails {

	/* this class is made to give type of authority and getDetails of entity */

	private User user;

	public CustomUserDetail(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// giving authority to user only
		SimpleGrantedAuthority simpleAuthority = new SimpleGrantedAuthority(user.getRole());

		List<SimpleGrantedAuthority> list = new ArrayList<SimpleGrantedAuthority>();
		list.add(simpleAuthority);
		return list;
		
		//for multiple roles
		/*
		 * Set<Role> roles = user.getRoles();
		 *  List<SimpleGrantedAuthority> authorities =
		 * new ArrayList<>();
		 * 
		 * for (Role role : roles) { authorities.add(new
		 * SimpleGrantedAuthority(role.getName())); }
		 */

	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
