package com.renato.projects.ecommerce.controller.dto.cliente;

import jakarta.validation.constraints.NotBlank;

public record PutClienteDTO(
		@NotBlank
		Long id,
		String nome, 
		String telefone) {

}
