package com.smartcontact.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontact.entities.User;

public interface UserRepo extends JpaRepository<User, Integer>{
	
	@Query("select u from User u where u.email = :email")
	User getUserByUserName(@Param("email") String email);
	
	Optional<User> findByEmail(String email);

}
