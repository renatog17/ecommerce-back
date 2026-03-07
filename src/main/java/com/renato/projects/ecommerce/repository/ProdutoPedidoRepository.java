package com.renato.projects.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.ProdutoPedido;

public interface ProdutoPedidoRepository extends JpaRepository<ProdutoPedido, Long> {

	Optional<ProdutoPedido> findByProdutoIdAndPedidoId(Long idProduto, Long idPedido);
	
	void deleteByProdutoIdAndPedidoId(Long idProduto, Long idPedido);
}
