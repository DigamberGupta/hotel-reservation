package com.digambergupta.hotelreservation.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.persistance.repository.ReservationRepository;
import com.digambergupta.hotelreservation.persistance.repository.RoomRepository;
import com.digambergupta.hotelreservation.persistance.repository.UserRepository;
import com.digambergupta.hotelreservation.service.ReservationService;
import com.digambergupta.hotelreservation.service.UserService;
import com.digambergupta.hotelreservation.validator.ReservationValidator;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@SpringBootTest
@ContextConfiguration
@Import(ReservationServiceImplITCase5.CustomReservationServiceConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class ReservationServiceImplITCase5 {

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

	@ParameterizedTest
	@CsvSource({ "1, 3, 1, ACCEPTED",
					   "0, 4, 2, ACCEPTED",
					   "2, 3, 3, DECLINE",
					   "5, 5, 4, ACCEPTED",
					   "4, 10, 5, DECLINE",
					   "10, 10, 6, ACCEPTED",
					   "6, 7, 7, ACCEPTED",
					   "8, 10, 8, ACCEPTED",
					   "8, 9, 9, ACCEPTED" })
	void reserveBooking(int checkInDays, int checkOutDays, int numberOfReservation, String status) {
		String actualStatus = reservationService.reserveBooking("test", LocalDate.now().plusDays(checkInDays), LocalDate.now().plusDays(checkOutDays));

		assertNotNull(actualStatus);
		assertEquals(status, actualStatus);
		assertEquals(numberOfReservation, userService.findByUsername("test").getReservations().size());
	}

	@TestConfiguration
	@ActiveProfiles("test")
	public static class CustomReservationServiceConfig {

		public static final int MAX_AVAILABLE_ROOM_FOR_BOOKING = 2;

		@Value("${booking.available.max.days}")
		private int maxNumberOfDaysForBooking;

		@Bean
		public ReservationService reservationService(UserRepository userRepository, ReservationRepository reservationRepository,
				RoomRepository roomRepository) {

			return new ReservationServiceImpl(userRepository, reservationRepository, roomRepository, new ReservationValidator(maxNumberOfDaysForBooking,
					MAX_AVAILABLE_ROOM_FOR_BOOKING));

		}
	}
}