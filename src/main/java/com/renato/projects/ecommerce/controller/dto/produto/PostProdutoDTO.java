package com.renato.projects.ecommerce.controller.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostProdutoDTO(
		@NotBlank(message = "Nome do produto não pode estar em branco")
		String nome,
		String descricao,
		@NotNull(message = "Quantidade não pode estar em branco")
		Long quantidade,
		@NotNull(message = "Preço do produto não pode estar em branco")
		BigDecimal preco,
		@NotNull(message = "Id da categoria não pode estar em branco")
		Long idCategoria
		) {

}
