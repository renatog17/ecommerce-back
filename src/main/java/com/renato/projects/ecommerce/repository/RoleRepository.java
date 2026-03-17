package com.renato.projects.ecommerce.repository;

import com.renato.projects.ecommerce.domain.Role;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.domain.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long>{

	Optional<Role> findByName(RoleName name);
}
