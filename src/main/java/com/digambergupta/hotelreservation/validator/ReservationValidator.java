package com.digambergupta.hotelreservation.validator;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ReservationValidator {

	private final LocalDate lastBookingDate;

	private final int maxAvailableRoomForBooking;

	public ReservationValidator(@Value("${booking.available.max.days}") int maxNumberOfDaysForBooking,
			@Value("${booking.rooms.available.max}") int maxAvailableRoomForBooking) {
		this.lastBookingDate = LocalDate.now().plusDays(maxNumberOfDaysForBooking);
		this.maxAvailableRoomForBooking = maxAvailableRoomForBooking;
	}

	public boolean isInvalidBookingDates(LocalDate checkInDate, LocalDate checkOutDate) {
		if (checkInDate == null || checkOutDate == null) {
			return true;
		}

		if (checkInDate.toEpochDay() < LocalDate.now().toEpochDay() || checkOutDate.toEpochDay() < LocalDate.now().toEpochDay()) {
			return true;
		}

		if (checkOutDate.toEpochDay() < checkInDate.toEpochDay()) {
			return true;
		}

		return checkInDate.toEpochDay() > lastBookingDate.toEpochDay() || checkOutDate.toEpochDay() > lastBookingDate.toEpochDay();
	}

	public int getMaxAvailableRoomForBooking() {
		return maxAvailableRoomForBooking;
	}
}
