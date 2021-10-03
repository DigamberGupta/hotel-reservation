package com.digambergupta.hotelreservation.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.service.ReservationService;
import com.digambergupta.hotelreservation.service.UserService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReservationServiceImpITCase1 {

	@Autowired
	private ReservationService reservationService;

	@Autowired
	private UserService userService;

	@BeforeEach
	void setUp() {
		if (userService.findByUsername("test") == null) {
			final User user = new User();
			user.setUsername("test");
			user.setEmail("test@test.com");
			user.setPassword("password");
			userService.save(user);
		}
	}

	@AfterEach
	void tearDown() {
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void reserveBooking_DECLINE_WHEN_INVALID_CHECKIN_DATES() {
		String status = reservationService.reserveBooking("test", LocalDate.now().minusDays(4), LocalDate.now().plusDays(2));

		assertNotNull(status);
		assertEquals(ReservationServiceImpl.STATUS_DECLINE, status);
	}

	@Test
	@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
	void reserveBooking_DECLINE_WHEN_INVALID_CHECKOUT_DATES() {
		String status = reservationService.reserveBooking("test", LocalDate.now().plusDays(200), LocalDate.now().plusDays(400));

		assertNotNull(status);
		assertEquals(ReservationServiceImpl.STATUS_DECLINE, status);
	}

}