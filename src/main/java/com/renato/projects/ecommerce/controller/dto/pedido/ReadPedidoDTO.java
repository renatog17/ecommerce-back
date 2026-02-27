package com.renato.projects.ecommerce.controller.dto.pedido;

import java.math.BigDecimal;
import java.util.List;

import com.renato.projects.ecommerce.controller.dto.produtopedido.ReadProdutoPedidoDTO;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.enums.Status;

public record ReadPedidoDTO(
		Long id,
		BigDecimal valorTotal,
		Status status,
		List<ReadProdutoPedidoDTO> itens
		) {

	public ReadPedidoDTO(Pedido pedido) {
		this(pedido.getId(), pedido.getValorTotal(), pedido.getStatus(), pedido.getProdutosPedidos().stream().map(ReadProdutoPedidoDTO::new).toList());
	}

}
