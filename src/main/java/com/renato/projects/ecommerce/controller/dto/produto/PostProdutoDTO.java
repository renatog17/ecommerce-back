package com.renato.projects.ecommerce.controller.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PostProdutoDTO(
		@NotBlank
		String nome,
		String descricao,
		@NotNull
		Long quantidade,
		@NotNull
		BigDecimal preco,
		Long idCategoria
		) {

}
