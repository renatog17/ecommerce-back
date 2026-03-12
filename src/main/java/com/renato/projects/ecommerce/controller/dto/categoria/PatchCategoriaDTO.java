package com.renato.projects.ecommerce.controller.dto.categoria;

import jakarta.validation.constraints.NotBlank;

public record PatchCategoriaDTO(
        @NotBlank( message =  "Descrição não pode estar em branco")
        String descricao
) {}