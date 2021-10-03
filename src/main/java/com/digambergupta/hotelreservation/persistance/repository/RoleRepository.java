package com.digambergupta.hotelreservation.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.digambergupta.hotelreservation.persistance.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

	@Query("Select r from Role r where r.name = :roleName")
	Role findRoleByName(@Param("roleName") String roleName);
}
