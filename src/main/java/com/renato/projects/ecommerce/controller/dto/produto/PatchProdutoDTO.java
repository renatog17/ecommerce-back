package com.renato.projects.ecommerce.controller.dto.produto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PatchProdutoDTO(

        @Size(min = 3, max = 100)
        String nome,

        @Positive
        Long quantidade,

        @DecimalMin(value = "0.0", inclusive = false)
        BigDecimal preco,

        @Positive
        Long idCategoria

) {}