package com.smartcontact.helper;

public class Message {  //this class is only to print the message at pages
	
	private String content;  //what is written in message
	private String type; // what type of message is 
	
	
	public Message(String content, String type) {
		super();
		this.content = content;
		this.type = type;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}
	
	
	
	

}
