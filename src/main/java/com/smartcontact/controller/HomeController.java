package com.smartcontact.controller;

import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontact.dao.UserRepo;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;
import com.smartcontact.service.EmailService;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordencoder;
	
	@Autowired
	private UserRepo userrepo;

	@Autowired
	private EmailService emailService;
	
	Random rndm = new Random(1000);
	
	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}

	// showing singup page
	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "Sign Up-Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	// handler for registration
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user, BindingResult result, Model m,
			@RequestParam(value = "checkbox", defaultValue = "false") boolean agree, HttpSession session) {
		try {

			if (result.hasErrors()) {
				System.out.println("Validation Problems");
				System.out.println(result);
				return "signup";
			}

			if (!agree) {
				System.out.println("You haven't agreed with Terms & Conditions");
				session.setAttribute("message", new Message("You haven't agreed with Terms & Conditions" , "alert-danger"));
				return "signup";
				//		throw new Exception("You haven't agreed with Terms & Conditions");
			}
			
			//checking uniqueness in database
			Optional<User> findByEmail = this.userrepo.findByEmail(user.getEmail());
			if(findByEmail.isPresent()) {
				session.setAttribute("message", new Message("Email Already Exists, try new one !!" , "alert-warning"));
				return "signup";
				
					//throw new Exception("Email Already Exist In System");
					 /* every time to throw exception is not good. Check if condition is true , just return it instead of 
					 * further page processing.
					 * */
			} 
			

			user.setRole("ROLE_USER");
			user.setImageUrl("default.png");
			user.setStatus(true);
			user.setPassword(passwordencoder.encode(user.getPassword()));  //encoding password
			
			System.out.println("Encoded password  "+user.getPassword());
		
				int nextInt = rndm.nextInt(999999);
				System.out.println("OTP is  : "+nextInt);
				String body = "This is OTP to confirm your account : "+nextInt;
				
				session.setAttribute("user", user);
				session.setAttribute("otp", nextInt);
				
				 this.emailService.sendMail(user.getEmail(), "Confirm Your Account", body);
				 	
			

			//User saveresult = this.userrepo.save(user);
			//System.out.println(saveresult);
			
			m.addAttribute("user", new User()); // sending empty object , if successfull fields willl be empty
			return "registration_verify";
		
		} catch (Exception e) {
			e.printStackTrace();
			m.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went Wrong !!" , "alert-danger"));
			return "signup";
		}
		
	}
	
	@PostMapping("/registration-verify")
	public String registrationVeri(@RequestParam("register") int otp, HttpSession httpSession ) {
	
		httpSession.removeAttribute("message");
		
		int regOtp = (int)httpSession.getAttribute("otp");
		User user = (User) httpSession.getAttribute("user");
		
		if(regOtp==otp) {
			System.out.println("otp match : "+regOtp+" and "+otp);
			
			userrepo.save(user);
			System.out.println("user saved "+user);
			
			return "redirect:/signin?change=Your Account is verified";
		}else {
			
			System.err.println("otp didn't match");
			httpSession.setAttribute("message", "Incorrect OTP");
			return "registration_verify";
		}
		
	}


	@GetMapping("/signin")
	public String login() {
		return "loginUser";
	}
}

