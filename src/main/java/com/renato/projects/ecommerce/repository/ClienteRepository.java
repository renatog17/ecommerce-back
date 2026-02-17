package com.renato.projects.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;

public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	Optional<Cliente> findByUserId(Long id);
	Optional<Cliente> findByUser(UserDetailsImpl user);
}
