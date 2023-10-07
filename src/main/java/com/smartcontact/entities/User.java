package com.smartcontact.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy  =GenerationType.AUTO)
	private int uid;
	
	@NotEmpty
	@Size(min = 5, message = "name must contain at-least 5 characters")
	private String name;
	
	@Column(length = 60)
	@Size( min = 8,max = 60, message = "password must be 8 to 15 character")
	private String password;
	
	
	private String role;
	
	@Column(unique = true)
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.com")
	private String email;
	
	private String phone;
	private String imageUrl;
	private boolean status;
	
	@Column(length =1000)
	private String about;
	
	@OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.LAZY, mappedBy = "user")
	private List<Contact> contact = new ArrayList<>();
	
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}


	public int getId() {
		return uid;
	}


	public void setId(int uid) {
		this.uid = uid;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}


	public boolean isStatus() {
		return status;
	}


	public void setStatus(boolean status) {
		this.status = status;
	}


	public String getAbout() {
		return about;
	}


	public void setAbout(String about) {
		this.about = about;
	}


	public List<Contact> getContact() {
		return contact;
	}

public void setContact(List<Contact> contact) {
		this.contact = contact;
	}


@Override
public String toString() {
	return "User [uid=" + uid + ", name=" + name + ", password=" + password + ", role=" + role + ", email=" + email
			+ ", phone=" + phone + ", imageUrl=" + imageUrl + ", status=" + status + ", about=" + about + ", contact="
			+ contact + "]";
}
	


	
	
}
