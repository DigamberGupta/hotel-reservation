package com.digambergupta.hotelreservation.persistance.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digambergupta.hotelreservation.persistance.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	User findByUsername(String username);

	@Query("SELECT user FROM User user WHERE user.username = :name")
	User findUserByName(@Param("name") String name);

	@Query("SELECT user FROM User user WHERE user.username LIKE %:name%")
	List<User> searchUsersByName(@Param("name") String name);

}
