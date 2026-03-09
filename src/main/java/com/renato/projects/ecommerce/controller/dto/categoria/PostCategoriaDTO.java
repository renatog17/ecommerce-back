package com.renato.projects.ecommerce.controller.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record PostCategoriaDTO(
        @NotBlank(message = "Nome da categoria não pode estar em branco")
        String nome,
        String descricao
) {}