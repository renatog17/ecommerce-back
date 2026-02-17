package com.renato.projects.ecommerce.controller.dto.pedido;

import java.util.List;

public record PostPedidoDTO(
		List<ItemPedido> itens
		) {

}
