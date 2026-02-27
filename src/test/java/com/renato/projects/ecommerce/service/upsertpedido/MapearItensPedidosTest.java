package com.renato.projects.ecommerce.service.upsertpedido;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.renato.projects.ecommerce.controller.dto.pedido.ItemPedido;
import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@ExtendWith(MockitoExtension.class)
public class MapearItensPedidosTest {

	@Mock
	private ProdutoRepository produtoRepository;

	@InjectMocks
	private MapearItensPedidos mapearItensPedidos;

	@Test
	void deveMapearProdutosComSuasQuantidades() {
		// Arrange
		ItemPedido item1 = new ItemPedido(1L, 2L);
		ItemPedido item2 = new ItemPedido(2L, 5L);

		PostPedidoDTO dto = new PostPedidoDTO(List.of(item1, item2));

		Produto produto1 = new Produto();
		produto1.setId(1L);

		Produto produto2 = new Produto();
		produto2.setId(2L);

		when(produtoRepository.findAllById(List.of(1L, 2L))).thenReturn(List.of(produto1, produto2));

		// Act
		Map<Produto, Long> resultado = mapearItensPedidos.toMap(dto);

		// Assert
		assertThat(resultado).hasSize(2);
		assertThat(resultado.get(produto1)).isEqualTo(2L);
		assertThat(resultado.get(produto2)).isEqualTo(5L);

		verify(produtoRepository).findAllById(List.of(1L, 2L));
	}

}
