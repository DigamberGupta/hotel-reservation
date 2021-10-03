package com.digambergupta.hotelreservation.persistance.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "room")
public class Room {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToMany
	private List<Reservation> reservations = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}

	public Reservation addReservation(final User user, final LocalDate checkInDate, final LocalDate checkOutDate) {
		if (checkInDate.compareTo(LocalDate.now()) >= 0 && checkOutDate.compareTo(LocalDate.now().minusDays(1L)) >= 0) {
			if (isRoomAvailable(checkInDate, checkOutDate)) {
				final Reservation reservation = new Reservation();
				reservation.setRoom(this);
				reservation.setCheckInDate(checkInDate);
				reservation.setCheckOutDate(checkOutDate);
				reservation.setAccepted(Boolean.TRUE);
				reservation.setUser(user);
				reservations.add(reservation);
				return reservation;
			}
		}
		return null;
	}

	public boolean isRoomAvailable(final LocalDate checkIn, final LocalDate checkOut) {
		if (reservations.isEmpty()) {
			return true;
		}

		List<Reservation> collect = reservations.stream().filter(
				reservation -> (reservation.getCheckInDate().compareTo(checkOut) < 0 && reservation.getCheckOutDate().compareTo(checkIn) < 0) || (
						reservation.getCheckInDate().compareTo(checkOut) > 0 && reservation.getCheckOutDate().compareTo(checkIn) > 0)).collect(
				Collectors.toList());

		if (collect.size() == reservations.size()) {
			return true;
		}

/*		boolean isAvailable = true;
		for(Reservation r: reservations){
			if(r.getCheckInDate().compareTo(checkOut)<=0 && r.getCheckOutDate().compareTo(checkIn)<=0)
				return true;
		}*/

/*		if (collect.size() == 0) {
			return true;
		}*/

		return false;
	}

}
