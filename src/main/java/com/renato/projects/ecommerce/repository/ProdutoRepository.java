package com.renato.projects.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

	Optional<Produto> findByIdAndAtivoTrue(Long id);

	Page<Produto> findAllByAtivoTrue(Pageable pageable);

	Page<Produto> findAllByCategoriaIdAndAtivoTrue(Long categoriaId, Pageable pageable);
}
