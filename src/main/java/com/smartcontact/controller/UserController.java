package com.smartcontact.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.smartcontact.dao.ContactRepo;
import com.smartcontact.dao.UserRepo;
import com.smartcontact.entities.Contact;
import com.smartcontact.entities.User;
import com.smartcontact.helper.Message;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserRepo repo;

	@Autowired
	private ContactRepo contactRepo;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@ModelAttribute // this method will automatically be called in every method
	public void showCommonData(Model m, Principal principal, @AuthenticationPrincipal OAuth2User authUser) {
		
		//chec if data already exits in our database. iF NOT getting data from OAuth2user
		User user = this.repo.getUserByUserName(principal.getName());
		
	if (Objects.nonNull(user)) {
		  
		m.addAttribute("user",user);	
						
		}else if (repo.getUserByUserName(authUser.getAttribute("email"))!=null) {
			
			m.addAttribute("user", repo.getUserByUserName(authUser.getAttribute("email")));
		
		} else {
			
			User userFromgoogle = new User();
			  
			  userFromgoogle.setEmail(authUser.getAttribute("email"));
			  userFromgoogle.setName(authUser.getAttribute("name"));
			  userFromgoogle.setImageUrl("default.png");
			  userFromgoogle.setAbout("Here to store contacts ");
			  userFromgoogle.setRole("ROLE_USER"); userFromgoogle.setStatus(true);
			  
			  
			  this.repo.save(userFromgoogle);
			  
			  m.addAttribute("user", userFromgoogle);
			  
			  }
			 
			
		/*
		 * principal.getName() will give us username which in this case is email
		 * address. we will fetch all data from database with the help of it
		 */
	}

	@GetMapping("/index")
	public String getLogin(Model m, @AuthenticationPrincipal OAuth2User u) {
		
		m.addAttribute("title", "User-Dashboard");
		return "normalUser/userDashboard";
	}

	@RequestMapping("/add-contact")
	public String addContact(Model m) {
		m.addAttribute("title", "User-Add Contact ");
		m.addAttribute("contact", new Contact());
		return "normalUser/addcontact";

	}

	@PostMapping("/process-contact")
	public String processContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			@RequestParam("cImageUrl") MultipartFile multipartFile, HttpSession session, @AuthenticationPrincipal OAuth2User principal,
			Model model) {
		try {

			User user = this.repo.getUserByUserName(principal.getAttribute("email"));
		

			//principal is logged in user
			// with help of principal we will get logged in user & insert contact in that
			// users

			if (bindingResult.hasErrors()) {
				System.out.println("Validation Problems " + bindingResult);
			}

			if (multipartFile.isEmpty()) {
				System.out.println("Image is empty");
				contact.setcImageUrl("default.png");
			} else {
				// setting image name with contact-ID in contact table
				contact.setcImageUrl(user.getId() + multipartFile.getOriginalFilename());

				// path of folder images will be stored
				String absolutePath = new ClassPathResource("static/img").getFile().getAbsolutePath();
				// path of image file
				Path path = Paths
						.get(absolutePath + File.separator + user.getId() + multipartFile.getOriginalFilename());

				System.out.println("This is specil casse " + path);
				StandardCopyOption replaceExisting = StandardCopyOption.REPLACE_EXISTING;
				System.out.println("Absolute Path" + absolutePath + "printing paths.get" + path + "\n Replace Existing "
						+ replaceExisting);

				Files.copy(multipartFile.getInputStream(), path, replaceExisting);
				System.out.println("file uploaded");
			}

			contact.setStatus(1);
			System.out.println("User " + user);
			contact.setUser(user); // adding parent to child, to which parent child be added
			System.out.println("contact " + contact);
			// adding that contact to user only when don't want to create ContactRepo
			// separately
			// user.getContact().add(contact);

			Contact save = contactRepo.save(contact);
			System.out.println(save);

			model.addAttribute("contact", new Contact());
			session.setAttribute("message", new Message("Contact Added Successfully", "alert-success"));

			return "normalUser/addcontact";
		}

		catch (Exception e) {
			e.printStackTrace();
			System.out.println("inside exceptions");
			model.addAttribute("c", contact);
			session.setAttribute("message", new Message("Something Went Wrong" + e.getMessage(), "alert-danger"));
			return "normalUser/addcontact";

		}

	}
	
	// current page = 0
	// number of records per page = 5
	@GetMapping("/view-contacts/{page}")
	public String view(Model model, @AuthenticationPrincipal OAuth2User principal, @PathVariable("page") Integer current_page) {
		User user = repo.getUserByUserName(principal.getAttribute("email")); // logged in user

		Pageable pageRequest = PageRequest.of(current_page, 5); // current page & 5 records per page
		
		Page<Contact> list_Ofcontacts = contactRepo.getContactByUser(user.getId(), pageRequest);
		
		model.addAttribute("totalPage", list_Ofcontacts.getTotalPages()); // total page
		model.addAttribute("currentPage", current_page);
		model.addAttribute("listcontact", list_Ofcontacts);
		
		return "normalUser/show_contacts";

	}

	// profile handler for contacts
	@GetMapping("/contact-profile/{cid}")
	public String profile(@PathVariable("cid") Integer id, Model m, @AuthenticationPrincipal OAuth2User  principal) {

		Optional<Contact> optional = this.contactRepo.findById(id);
		Contact contact = optional.get();

		User user = this.repo.getUserByUserName(principal.getAttribute("email"));

		if (user.getId() == contact.getUser().getId()) {
			m.addAttribute("c", contact);
			m.addAttribute("title", "Profile-Smart Contact Manager");
			// sending contact only for current user
		} else {
			m.addAttribute("title", "Error Page");
		}

		return "normalUser/profileContacts";
	}

	// delete contact Handler
	//@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid") int cid) {
		Contact contact = this.contactRepo.findById(cid).get();
		contact.setStatus(0);
		System.out.println("Updating contact to show user as if it is deleted");
		this.contactRepo.save(contact);
		return "redirect:/user/view-contacts/0";
	}

	// open update form
	@RequestMapping("/update-form/{cid}")
	public String openUpdate(@PathVariable("cid") int id, Model model, @AuthenticationPrincipal OAuth2User principal) {
		User user = repo.getUserByUserName(principal.getAttribute("email"));
		model.addAttribute("title", "Update Contact-Smart Contact Manager");
		Contact contact = contactRepo.findById(id).get();
		if (user.getId() == contact.getUser().getId()) {
			model.addAttribute("contact", contact);

		} else {
			model.addAttribute("title", "Error Page");
			System.out.println("you've no permission to edit this contact");
		}

		return "normalUser/update_form";
	}

	@PostMapping("/update-contact")
	public String processUpdate(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult,
			@AuthenticationPrincipal OAuth2User principal, @RequestParam("cImageUrl")  MultipartFile multiPartFile,HttpSession session) {
		
		try {
				User user = repo.getUserByUserName(principal.getAttribute("email"));
				
				
				  if (bindingResult.hasErrors()) {
				  System.out.println("Validation Errors inside Update"); }
				 
			
			if (multiPartFile.isEmpty()) {
				System.out.println("Image not found");
				contact.setcImageUrl("default.png");
			}else {
				//uploading Image 
				//setting path name of file   into contact
				contact.setcImageUrl(contact.getCid()+multiPartFile.getOriginalFilename());
				String absolutePath = new ClassPathResource("static/img/").getFile().getAbsolutePath();
				 //image path & name prefix is  contact ID
				Path path = Paths.get(absolutePath+File.separator+contact.getCid()+multiPartFile.getOriginalFilename());
				Files.copy(multiPartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				//reading file, name of image with contact Id, replace if same name exists
				
				
				/*
				 * File old_file = new ClassPathResource("static/img").getFile(); File file =
				 * new File(old_file, old_contact.getcImageUrl()); file.delete();
				 */			}
			
			contact.setUser(user);
			contact.setStatus(1); //active user
			
			contactRepo.save(contact);
			
			return "redirect:/user/view-contacts/0";
			// redirect target the url not the form/page

		} catch (Exception e) {
			e.printStackTrace();
			return "normalUser/update_form";
		}
	}
	
	//user profile opening 
	@GetMapping("/user-profile")
	public String userProfile(Model m, @AuthenticationPrincipal OAuth2User oauth) {
		
		User user = repo.getUserByUserName(oauth.getAttribute("email"));
			
			m.addAttribute("title", "User Profile");
			m.addAttribute("user", user);
		return "normalUser/user_profile";
	}
	
	@GetMapping("/user-profile-update")
	public String user_ProfileU(Model m, @AuthenticationPrincipal OAuth2User oauth) {
			User user = repo.getUserByUserName(oauth.getAttribute("email"));
		
		m.addAttribute("title", "User Profile Update");
		m.addAttribute("user", user);
		return "normalUser/user_profileUpdate";
	}
	
	@PostMapping("/update-user")
	public String u_profileProcess(@Valid  @ModelAttribute User user, BindingResult result, @RequestParam("imageUrl") MultipartFile multipartFile) {
		System.out.println("User data "+user);
		
		try {
			
				if (result.hasErrors()) {
					System.out.println("Validation problems");
					
				}
			
			if (!multipartFile.isEmpty()) {
				System.out.println("Image file is not empty");
				
				String file = new ClassPathResource("static/user_img").getFile().getAbsolutePath();
				
				Path path = Paths.get(file+File.separator+ user.getId()+ multipartFile.getOriginalFilename());
				
				Files.copy(multipartFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
				
				System.out.println("before saving image url in user");
				
				user.setImageUrl(user.getId()+multipartFile.getOriginalFilename()); //appending user ID with file name
				
				System.out.println("after saving image url  ");
				//deleting if picture already exists
				
				/*
				 * User old_user = new User();
				 * 
				 * File old_file = new ClassPathResource("static/user_img").getFile();
				 * 
				 * File deleteFile = new File(old_file, old_user.getImageUrl());
				 * deleteFile.delete();
				 */
				System.out.println("after deleting old file");
				
				
			}else {
				System.out.println("image not found");
				user.setImageUrl("default.png");
			}
			
			
			User u = repo.save(user);
			System.out.println("after saaaving data "+u);
			
			return "redirect:/user/user-profile";
			
		} catch (Exception e) {
			return "normalUser/user-profileUpdate";
		}
		
	}
	
	@GetMapping("/settings")
	public String setopen(Model m) {
		m.addAttribute("title", "Smart-Settings");
		return "normalUser/settings";
		
	}
	
	@PostMapping("/change-password")
	public String ChangePass(@RequestParam("oldpass") String old, 
			@RequestParam("newpass") String newpass, @RequestParam("confirmpass") String confirm,
			@AuthenticationPrincipal OAuth2User principal, HttpSession session) {
		
		User user = repo.getUserByUserName(principal.getAttribute("email"));
		
		String userPass = user.getPassword();
		if (bCryptPasswordEncoder.matches(old, userPass)) {
			
			if (newpass.matches(confirm)) {
				
				String pass = bCryptPasswordEncoder.encode(newpass);
				user.setPassword(pass);
				
				repo.save(user);
				
			}else {
			
				session.setAttribute("message", new Message("Confirm Password incorrect", "alert-danger"));
				return "normalUser/settings";
			
			}
		
		}else {
			session.setAttribute("message", new Message("Invalid Old Password", "alert-danger"));
			return "normalUser/settings";
			
		}
		
		return "redirect:/user/index";
	}
	
}