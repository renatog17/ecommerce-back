package com.renato.projects.ecommerce.controller.dto.produto;

import java.math.BigDecimal;

import com.renato.projects.ecommerce.controller.dto.categoria.ReadCategoriaDTO;
import com.renato.projects.ecommerce.domain.Produto;

public record ReadProdutoDTO(
		Long id,
		String nome,
		String descricao,
		Long quantidade,
		BigDecimal preco,
		ReadCategoriaDTO categoria
		) {

	public ReadProdutoDTO(Produto produto) {
		this(
			produto.getId(),
			produto.getNome(),
			produto.getDescricao(),
			produto.getQuantidade(),
			produto.getPreco(),
			new ReadCategoriaDTO(produto.getCategoria())
		);
		
	}
	
}
