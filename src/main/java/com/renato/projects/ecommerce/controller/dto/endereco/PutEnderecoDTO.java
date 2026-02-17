package com.renato.projects.ecommerce.controller.dto.endereco;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PutEnderecoDTO(

        @NotNull(message = "Id é obrigatório")
        Long id,

        @Size(max = 150, message = "Logradouro deve ter no máximo 150 caracteres")
        String logradouro,

        @Size(max = 20, message = "Número deve ter no máximo 20 caracteres")
        String numero,

        @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
        String complemento,

        @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
        String bairro,

        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String cidade,

        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (UF)")
        String estado,

        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String cep

) {}

