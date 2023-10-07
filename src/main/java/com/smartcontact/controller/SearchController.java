package com.smartcontact.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.smartcontact.dao.ContactRepo;
import com.smartcontact.dao.UserRepo;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;

@RestController
public class SearchController {

	@Autowired
	private UserRepo userRepo;
	@Autowired
	private ContactRepo contactRepo;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query") String q, Principal principal) {
		try {
				User user = userRepo.getUserByUserName(principal.getName());

			System.out.println("printing query " + q);
			System.out.println("user Id "+user.getId());
			List<Contact> search_contacts = contactRepo.findByNameLikeAndUser(q, user.getId());
			System.out.println(search_contacts.size());
			
			search_contacts.forEach(System.out::println);
			
			return ResponseEntity.ok(search_contacts);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.ok("Inside Exception");
		}
	}
}
