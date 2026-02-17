package com.renato.projects.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
