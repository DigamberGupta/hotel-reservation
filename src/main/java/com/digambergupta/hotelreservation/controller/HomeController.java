package com.digambergupta.hotelreservation.controller;

import static com.digambergupta.hotelreservation.util.ControllerUtil.HOME_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.REDIRECT;
import static com.digambergupta.hotelreservation.util.ControllerUtil.ROOT_PATH;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.digambergupta.hotelreservation.model.ReservationInfo;
import com.digambergupta.hotelreservation.persistance.entity.Reservation;
import com.digambergupta.hotelreservation.service.SecurityService;

@Controller
public class HomeController {

	private final SecurityService securityService;

	@Autowired
	public HomeController(final SecurityService securityService) {
		this.securityService = securityService;
	}

	@GetMapping({ ROOT_PATH, HOME_PATH })
	public String welcome(Model model) {
		if (!securityService.isAuthenticated()) {
			return REDIRECT + ROOT_PATH;
		}

		model.addAttribute("reservationForm", new ReservationInfo());
		model.addAttribute("reservations", Set.of(new Reservation()));

		return HOME_PATH;
	}

}
