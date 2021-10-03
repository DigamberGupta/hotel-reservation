package com.digambergupta.hotelreservation.service.impl;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.digambergupta.hotelreservation.persistance.entity.Role;
import com.digambergupta.hotelreservation.persistance.entity.User;
import com.digambergupta.hotelreservation.persistance.repository.RoleRepository;
import com.digambergupta.hotelreservation.persistance.repository.UserRepository;
import com.digambergupta.hotelreservation.service.UserService;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	@Transactional
	public void save(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		List<Role> roles = roleRepository.findAll();

		if (CollectionUtils.isEmpty(roles)) {
			Role role = new Role();
			role.setName("USER");
			Role save = roleRepository.save(role);
			roles.add(save);
		}

		user.setRoles(new HashSet<>(roles));
		userRepository.save(user);
	}

	@Override
	@Transactional
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
}