package com.renato.projects.ecommerce.controller.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record AlterarPrecoProdutoDTO(
		@NotNull(message = "O novo preço não pode ser null")
		@Positive(message = "Preço precisa ser maior que zero")
		BigDecimal novoPreco) {

}
