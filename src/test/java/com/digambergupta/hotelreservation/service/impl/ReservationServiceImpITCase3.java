package com.digambergupta.hotelreservation.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
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
class ReservationServiceImpITCase3 {

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

	@ParameterizedTest
	@CsvSource({ "0,5, 1, ACCEPTED", "2, 5, 2, ACCEPTED", "1,9, 3, ACCEPTED", "0, 15, 4, DECLINE" })
	void reserveBooking(int checkInDays, int checkOutDays, int numberOfReservation, String status) {
		String actualStatus = reservationService.reserveBooking("test", LocalDate.now().plusDays(checkInDays), LocalDate.now().plusDays(checkOutDays));

		assertNotNull(actualStatus);
		assertEquals(status, actualStatus);
		assertEquals(numberOfReservation, userService.findByUsername("test").getReservations().size());
	}

}