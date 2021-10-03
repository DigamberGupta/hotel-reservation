package com.digambergupta.hotelreservation.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.digambergupta.hotelreservation.model.ReservationInfo;
import com.digambergupta.hotelreservation.model.UserForm;
import com.digambergupta.hotelreservation.persistance.entity.Reservation;
import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.service.SecurityService;
import com.digambergupta.hotelreservation.service.UserService;
import com.digambergupta.hotelreservation.validator.UserValidator;

@Controller
public class UserController {

	private final UserService userService;

	private final SecurityService securityService;

	private final UserValidator userValidator;

	@Autowired
	public UserController(final UserService userService, final SecurityService securityService, final UserValidator userValidator) {
		this.userService = userService;
		this.securityService = securityService;
		this.userValidator = userValidator;
	}

	@GetMapping("/registration")
	public String registration(Model model) {
		if (securityService.isAuthenticated()) {
			return "redirect:/";
		}

		model.addAttribute("userForm", new User());

		return "registration";
	}

	@PostMapping("/registration")
	public String registration(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return "registration";
		}

		User user = new User();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.setEmail(userForm.getEmail());

		userService.save(user);

		securityService.autoLogin(userForm.getUsername(), userForm.getPassword());

		return "redirect:/home";
	}

	@GetMapping("/login")
	public String login(Model model, String error, String logout) {
		if (securityService.isAuthenticated()) {
			return "redirect:/";
		}

		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return "login";
	}
}