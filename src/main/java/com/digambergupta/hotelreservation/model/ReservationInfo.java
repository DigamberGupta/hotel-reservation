package com.digambergupta.hotelreservation.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationInfo {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate checkinDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate checkoutDate;

	public String status;

	public String error;

}
