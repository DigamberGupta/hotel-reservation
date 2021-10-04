package com.digambergupta.hotelreservation.persistance.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "room")
@Getter
@Setter
@NoArgsConstructor
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	private List<Reservation> reservations = new ArrayList<>();

	public Optional<Reservation> addReservation(final User user, final LocalDate checkInDate, final LocalDate checkOutDate) {
		if (checkInDate.compareTo(LocalDate.now()) >= 0 && checkOutDate.compareTo(LocalDate.now().minusDays(1L)) >= 0) {
			if (isRoomAvailable(checkInDate, checkOutDate)) {
				final Reservation reservation = new Reservation();
				reservation.setRoom(this);
				reservation.setCheckInDate(checkInDate);
				reservation.setCheckOutDate(checkOutDate);
				reservation.setAccepted(Boolean.TRUE);
				reservation.setUser(user);
				reservations.add(reservation);
				return Optional.of(reservation);
			}
		}
		return Optional.empty();
	}

	public boolean isRoomAvailable(final LocalDate checkIn, final LocalDate checkOut) {
		if (reservations.isEmpty()) {
			return true;
		}

		final List<Reservation> collect = reservations.stream()
				.filter(getReservationPredicate(checkIn, checkOut))
				.collect(Collectors.toList());

		return collect.size() == reservations.size();
	}

	private Predicate<Reservation> getReservationPredicate(LocalDate checkIn, LocalDate checkOut) {
		return reservation -> (reservation.getCheckInDate().compareTo(checkOut) < 0 && reservation.getCheckOutDate().compareTo(checkIn) < 0) || (
				reservation.getCheckInDate().compareTo(checkOut) > 0 && reservation.getCheckOutDate().compareTo(checkIn) > 0);
	}

}
