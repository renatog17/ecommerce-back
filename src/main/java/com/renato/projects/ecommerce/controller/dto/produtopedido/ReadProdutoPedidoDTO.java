package com.renato.projects.ecommerce.controller.dto.produtopedido;

import java.math.BigDecimal;

import com.renato.projects.ecommerce.domain.ProdutoPedido;

public record ReadProdutoPedidoDTO(
		Long id,
		BigDecimal valorUnitario,
		Long quantidade,
		Long produtoId
		) {
	
	public ReadProdutoPedidoDTO(ProdutoPedido pp) {
		this(pp.getId(), pp.getValorUnitario(), pp.getQuantidade(), pp.getProduto().getId());
	}

}
