package com.digambergupta.hotelreservation.service;

import com.digambergupta.hotelreservation.persistance.entity.User;

public interface UserService {
	void save(User user);

	User findByUsername(String username);
}