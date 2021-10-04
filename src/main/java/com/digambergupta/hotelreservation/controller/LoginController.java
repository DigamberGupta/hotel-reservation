package com.digambergupta.hotelreservation.controller;

import static com.digambergupta.hotelreservation.util.ControllerUtil.HOME_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.LOGIN_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.REDIRECT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.digambergupta.hotelreservation.service.SecurityService;

@Controller
public class LoginController {

	private final SecurityService securityService;

	@Autowired
	public LoginController(final SecurityService securityService) {
		this.securityService = securityService;
	}

	@GetMapping(LOGIN_PATH)
	public String login(Model model, String error, String logout) {
		if (securityService.isAuthenticated()) {
			return REDIRECT + HOME_PATH;
		}

		if (error != null)
			model.addAttribute("error", "Your username and password is invalid.");

		if (logout != null)
			model.addAttribute("message", "You have been logged out successfully.");

		return LOGIN_PATH;
	}

}
