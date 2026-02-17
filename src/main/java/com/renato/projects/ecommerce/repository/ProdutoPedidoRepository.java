package com.renato.projects.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.ProdutoPedido;

public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Long> {

}
