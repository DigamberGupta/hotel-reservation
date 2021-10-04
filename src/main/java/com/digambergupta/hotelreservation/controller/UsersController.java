package com.digambergupta.hotelreservation.controller;

import static com.digambergupta.hotelreservation.util.ControllerUtil.HOME_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.REDIRECT;
import static com.digambergupta.hotelreservation.util.ControllerUtil.REGISTRATION_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.RESERVATION_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.ROOT_PATH;
import static com.digambergupta.hotelreservation.util.ControllerUtil.USERS_PATH;

import java.util.List;

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
import com.digambergupta.hotelreservation.model.UserForm;
import com.digambergupta.hotelreservation.persistance.entity.Reservation;
import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.service.ReservationService;
import com.digambergupta.hotelreservation.service.SecurityService;
import com.digambergupta.hotelreservation.service.UserService;
import com.digambergupta.hotelreservation.validator.UserValidator;

@Controller(USERS_PATH)
public class UsersController {
	private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

	private final UserService userService;
	private final SecurityService securityService;
	private final ReservationService reservationService;
	private final UserValidator userValidator;

	@Autowired
	public UsersController(final UserService userService, final SecurityService securityService, final UserValidator userValidator,
			final ReservationService reservationService) {
		this.userService = userService;
		this.securityService = securityService;
		this.userValidator = userValidator;
		this.reservationService = reservationService;
	}

	@GetMapping(REGISTRATION_PATH)
	public String registration(Model model) {
		if (securityService.isAuthenticated()) {
			return REDIRECT + ROOT_PATH;
		}

		model.addAttribute("userForm", new User());

		return REGISTRATION_PATH;
	}

	@PostMapping(REGISTRATION_PATH)
	public String registration(@ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
		userValidator.validate(userForm, bindingResult);

		if (bindingResult.hasErrors()) {
			return REGISTRATION_PATH;
		}

		final User user = new User();
		user.setUsername(userForm.getUsername());
		user.setPassword(userForm.getPassword());
		user.setEmail(userForm.getEmail());

		userService.save(user);
		securityService.autoLogin(userForm.getUsername(), userForm.getPassword());

		return REDIRECT + HOME_PATH;
	}

	@PostMapping(RESERVATION_PATH)
	public String doReservation(@AuthenticationPrincipal UserDetails userDetails, @ModelAttribute("reservation") ReservationInfo reservation,
			BindingResult bindingResult, Model model) {
		if (isUnAuthenticatedUser(userDetails))
			return REDIRECT + ROOT_PATH;

		final String bookingStatus = reservationService.reserveBooking(userDetails.getUsername(), reservation.getCheckinDate(), reservation.getCheckoutDate());
		reservation.setStatus(bookingStatus);
		reservation.setError(null);
		model.addAttribute("reservationForm", reservation);

		return HOME_PATH;
	}

	@GetMapping(RESERVATION_PATH)
	public String getReservations(@AuthenticationPrincipal UserDetails userDetails, Model model) {
		if (isUnAuthenticatedUser(userDetails))
			return REDIRECT + ROOT_PATH;

		final List<Reservation> reservations = reservationService.getBookingsByUser(userDetails.getUsername());
		model.addAttribute("reservations", reservations);

		return RESERVATION_PATH;
	}

	private boolean isUnAuthenticatedUser(@AuthenticationPrincipal UserDetails userDetails) {
		if (!securityService.isAuthenticated()) {
			LOGGER.info("User " + userDetails.getUsername() + " is not authenticated");
			return true;
		}
		return false;
	}

}