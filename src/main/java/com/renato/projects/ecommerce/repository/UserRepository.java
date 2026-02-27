package com.renato.projects.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.UserDetailsImpl;

public interface UserRepository extends JpaRepository<UserDetailsImpl, Long>{

	Optional<UserDetailsImpl> findByEmail(String email);
	Optional<UserDetailsImpl> findByVerificationToken(String verificationToken);
}
