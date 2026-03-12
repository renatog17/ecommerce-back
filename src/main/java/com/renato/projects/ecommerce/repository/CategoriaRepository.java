package com.renato.projects.ecommerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

	List<Categoria> findAllByAtivoTrue(Boolean ativo);

	Optional<Categoria> findByIdAndAtivoTrue(Long id, boolean b);

}
