package com.digambergupta.hotelreservation.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.digambergupta.hotelreservation.persistance.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
}
