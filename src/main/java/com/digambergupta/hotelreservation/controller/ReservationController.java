package com.digambergupta.hotelreservation.controller;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.digambergupta.hotelreservation.model.ReservationInfo;
import com.digambergupta.hotelreservation.persistance.entity.Reservation;
import com.digambergupta.hotelreservation.service.ReservationService;
import com.digambergupta.hotelreservation.service.SecurityService;

@Controller
public class ReservationController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ReservationController.class);

	private final ReservationService reservationService;

	private final SecurityService securityService;

	@Autowired
	public ReservationController(final ReservationService reservationService,
			final SecurityService securityService) {
		this.reservationService = reservationService;
		this.securityService = securityService;
	}

	@GetMapping({ "/", "/home" })
	public String welcome(Model model) {
		if (!securityService.isAuthenticated()) {
			return "redirect:/";
		}

		model.addAttribute("reservationForm", new ReservationInfo());
		model.addAttribute("reservations", Set.of(new Reservation()));

		return "home";
	}

	@PostMapping("/reservation")
	public String doReservation(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("reservation") ReservationInfo reservation,
			BindingResult bindingResult, Model model) {
		if (!securityService.isAuthenticated()) {
			return "redirect:/";
		}

		final String bookingStatus = reservationService.reserveBooking(userDetails.getUsername(), reservation.getCheckinDate(), reservation.getCheckoutDate());
		reservation.setStatus(bookingStatus);
		reservation.setError(null);
		model.addAttribute("reservationForm", reservation);

		return "home";
	}

	@GetMapping("/booking")
	public String booking(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (!securityService.isAuthenticated()) {
			return "redirect:/";
		}

		final List<Reservation> reservations = reservationService.getBookingsByUser(userDetails.getUsername());
		model.addAttribute("reservations", reservations);

		return "booking";
	}

}
