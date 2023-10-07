package com.smartcontact.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Contact {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int cid;
	
	@NotEmpty(message = "Empty !!")
	@Size(min = 8, message = "name must be 8 character at least")
	private String name;
	@Size(min = 3, message = "Nick name be at least 3 characters")
	private String nickName;
	
	private String work;
	
	@Pattern(regexp = "[+]*[9,2]*[0,3]{2}[0-9]{2}\\-[0-9]{7}", message = "Put number in correct format")
	private String number;
	
	@Email(regexp = "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+\\.com")
	private String email;
	private String cImageUrl;
	@Column(length = 100)
	private String description;
	
	private int Status;

	
	@ManyToOne
	@JsonIgnore
	private User user;
	
	public Contact() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getCid() {
		return cid;
	}

	public void setCid(int cid) {
		this.cid = cid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getNumber() {
		return number;
	}

	public void setnumber(String cnumber) {
		this.number = cnumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getcImageUrl() {
		return cImageUrl;
	}

	public void setcImageUrl(String cImageUrl) {
		this.cImageUrl = cImageUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Contact [cid=" + cid + ", name=" + name + ", nickName=" + nickName + ", work=" + work + ", number="
				+ number + ", email=" + email + ", cImageUrl=" + cImageUrl + ", description=" + description
				+ ", Status=" + Status +  "]";
	}

	
	
	
	
}
