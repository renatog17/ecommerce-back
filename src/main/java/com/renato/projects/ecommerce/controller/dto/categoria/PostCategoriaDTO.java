package com.renato.projects.ecommerce.controller.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record PostCategoriaDTO(
        @NotBlank
        String nome,
        String descricao
) {}