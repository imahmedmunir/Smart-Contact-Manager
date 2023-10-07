package com.smartcontact.controller;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.dao.UserRepo;
import com.smartcontact.entities.User;
import com.smartcontact.service.EmailService;

@Controller
public class ForgotController {

	@Autowired
	private EmailService emailService;

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	Random random = new Random(1000);

	@GetMapping("/forgot-password")
	public String forgotPass(Model m) {
		m.addAttribute("title", "Forgot Password-Smart Contact Manager");
		return "forgotPassword";
	}

	@PostMapping("/password-otp")
	public String pass_otp(@RequestParam("emailotp") String email, HttpSession httpSession) {

		System.out.println(email);

		Optional<User> user = userRepo.findByEmail(email);
		if (user.isPresent()) {
			int otp = random.nextInt(9999);
			System.out.println("opt " + otp);

			// sending email here
			String to = email;
			String subject = "Confirm Registration - Smart Contact Manager";
			String body = "This is your OTP  " + otp;

			boolean flag = this.emailService.sendMail(to, subject, body);
			// storing value in session
			httpSession.setAttribute("Session_otp", otp);
			httpSession.setAttribute("Session_email", email);

			System.out.println("  printing..........flag : " + flag);

			return "verify_otp";
		}
		
		httpSession.setAttribute("message", "This email is not registered");
		return "forgotPassword";
	}

	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("verifyotp") int otp, HttpSession httpSession) {

		int myotp = (int) httpSession.getAttribute("Session_otp");
		System.out.println("Session Store otp" + myotp + " and sent otp " + otp);

		if (myotp == otp) {
			System.out.println("otp match.................");

			return "change_password";

		} else {
			httpSession.setAttribute("message", "Incorrect OTP");
			return "verify_otp";
		}

	}

	@PostMapping("/change-pass")
	public String changePass(@RequestParam("pass") String pass, @RequestParam("confirmpass") String confirm,
			HttpSession httpSession) {

		if (pass.matches(confirm)) {

			String email = (String) httpSession.getAttribute("Session_email");
			System.out.println("password Matched and email to save is  " + email);

			User user = userRepo.getUserByUserName(email);
			System.out.println("User is " + user);
			
			user.setPassword(bCryptPasswordEncoder.encode(pass));

			 userRepo.save(user);
			 System.out.println("password Changed...........");
			 return "redirect:/signin?change= Password changed Successfully";
			 
		}else {
			httpSession.setAttribute("message", "Password did not match");
			return "change_password";
		}

		
	}

}
