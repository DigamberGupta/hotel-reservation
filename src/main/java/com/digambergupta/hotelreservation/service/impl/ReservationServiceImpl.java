package com.digambergupta.hotelreservation.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.digambergupta.hotelreservation.persistance.entity.Reservation;
import com.digambergupta.hotelreservation.persistance.entity.Room;
import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.persistance.repository.ReservationRepository;
import com.digambergupta.hotelreservation.persistance.repository.RoomRepository;
import com.digambergupta.hotelreservation.persistance.repository.UserRepository;
import com.digambergupta.hotelreservation.service.ReservationService;
import com.digambergupta.hotelreservation.validator.ReservationValidator;

@Service
public class ReservationServiceImpl implements ReservationService {

	public static final String STATUS_ACCEPTED = "ACCEPTED";
	public static final String STATUS_DECLINE = "DECLINE";
	private final UserRepository userRepository;

	private final ReservationRepository reservationRepository;

	private final RoomRepository roomRepository;

	private final ReservationValidator reservationValidator;

	public ReservationServiceImpl(UserRepository userRepository,
			ReservationRepository reservationRepository, RoomRepository roomRepository,
			ReservationValidator reservationValidator) {
		this.userRepository = userRepository;
		this.reservationRepository = reservationRepository;
		this.roomRepository = roomRepository;
		this.reservationValidator = reservationValidator;
	}

	@Override
	@Transactional
	public String reserveBooking(String username, LocalDate checkInDate, LocalDate checkOutDate) {
		final User user = userRepository.findByUsername(username);

		if (reservationValidator.isInvalidBookingDates(checkInDate, checkOutDate)) {
			saveDeclinedReservation(checkInDate, checkOutDate, user);
			return STATUS_DECLINE;
		}

		final List<Room> rooms = roomRepository.findAll();

		if (addReservationForCreatedRooms(checkInDate, checkOutDate, user, rooms)) {
			return STATUS_ACCEPTED;
		}

		if (createNewAndAddReservation(checkInDate, checkOutDate, user, rooms)) {
			return STATUS_ACCEPTED;
		}

		saveDeclinedReservation(checkInDate, checkOutDate, user);

		return STATUS_DECLINE;
	}

	private void saveDeclinedReservation(final LocalDate checkInDate, final LocalDate checkOutDate, final User user) {
		final Reservation reservation = new Reservation();
		reservation.setAccepted(Boolean.FALSE);
		reservation.setUser(user);
		reservation.setCheckInDate(checkInDate);
		reservation.setCheckOutDate(checkOutDate);
		user.addReservation(reservation);
		reservationRepository.save(reservation);
	}

	private boolean createNewAndAddReservation(LocalDate checkInDate, LocalDate checkOutDate, User user, List<Room> rooms) {
		if (rooms.size() < reservationValidator.getMaxAvailableRoomForBooking()) {
			final Room room = roomRepository.save(new Room());
			addReservationForCreatedRooms(checkInDate, checkOutDate, user, List.of(room));
			return true;
		}
		return false;
	}

	private boolean addReservationForCreatedRooms(LocalDate checkInDate, LocalDate checkOutDate, User user, List<Room> rooms) {
		for (Room room : rooms) {
			final Optional<Reservation> reservation = room.addReservation(user, checkInDate, checkOutDate);
			if (reservation.isPresent()) {
				user.addReservation(reservation.get());
				reservationRepository.save(reservation.get());
				roomRepository.save(room);
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Reservation> getBookingsByUser(final String username) {
		final User user = userRepository.findUserByName(username);

		if (user == null) {
			return List.of();
		}

		return user.getReservations();
	}

}
