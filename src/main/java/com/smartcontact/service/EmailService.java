package com.smartcontact.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

	@Autowired
	private JavaMailSender javaMailSender;
	
	public boolean sendMail(String to, String sub, String body) {
		
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		
		
		mailMessage.setFrom("munirsecondid@gmail.com");
		mailMessage.setTo(to);
		mailMessage.setSubject(sub);
		mailMessage.setText(body);
	
		
		 javaMailSender.send(mailMessage);
		
		 System.out.println("Email sent successfully..........");
		 
		return true;
		
	}
}
