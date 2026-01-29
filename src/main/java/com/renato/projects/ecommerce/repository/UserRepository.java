package com.renato.projects.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.User;

public interface UserRepository extends JpaRepository<User, Long>{

	Optional<User> findByEmail(String email);
	Optional<User> findByVerificationToken(String verificationToken);
}
