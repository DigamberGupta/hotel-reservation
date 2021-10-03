package com.digambergupta.hotelreservation.service;

import java.time.LocalDate;
import java.util.List;

import com.digambergupta.hotelreservation.persistance.entity.Reservation;

public interface ReservationService {
	String reserveBooking(String username, LocalDate start, LocalDate end);

	List<Reservation> getBookingsByUser(String username);
}
