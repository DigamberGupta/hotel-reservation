package com.digambergupta.hotelreservation.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public class ReservationInfo {

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate checkinDate;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	public LocalDate checkoutDate;

	public String status;

	public String error;

	public LocalDate getCheckinDate() {
		return checkinDate;
	}

	public void setCheckinDate(LocalDate checkinDate) {
		this.checkinDate = checkinDate;
	}

	public LocalDate getCheckoutDate() {
		return checkoutDate;
	}

	public void setCheckoutDate(LocalDate checkoutDate) {
		this.checkoutDate = checkoutDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
}
