package com.renato.projects.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

}
