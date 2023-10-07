package com.smartcontact.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.smartcontact.entities.Contact;

public interface ContactRepo extends JpaRepository<Contact, Integer>{
	
	//value="SELECT cid, NAME, number, email, WORK FROM contact WHERE user_uid =userId AND STATUS = 1 ", nativeQuery = true
	//"select c from Contact c  where c.user.id=:userId and c.Status=1 "
	//from contact as c where c.user.id=:usid
	
	@Query("select c from Contact c  where c.user.id=:userId and c.Status=1")   //necessary to know where from you're fetching
		Page<Contact> getContactByUser(@Param("userId") int id, Pageable pageable);

	//"select c from Contact c where c.name LIKE :name% and c.Status=1 and c.user.uid=:userId"
	//when using native Query, make sure to use name as described in sql table
		@Query(value="select * from Contact  where name LIKE :name% and Status=1 and user_uid=:userId", nativeQuery = true)
		public List<Contact> findByNameLikeAndUser(@Param("name") String name, @Param("userId") int id);
	
}
