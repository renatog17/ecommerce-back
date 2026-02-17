package com.renato.projects.ecommerce.controller.dto.endereco;

import com.renato.projects.ecommerce.domain.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record PostEnderecoDTO(

        @NotBlank(message = "Logradouro é obrigatório")
        @Size(max = 150, message = "Logradouro deve ter no máximo 150 caracteres")
        String logradouro,

        @Size(max = 20, message = "Número deve ter no máximo 20 caracteres")
        String numero,

        @Size(max = 100, message = "Complemento deve ter no máximo 100 caracteres")
        String complemento,

        @NotBlank(message = "Bairro é obrigatório")
        @Size(max = 100, message = "Bairro deve ter no máximo 100 caracteres")
        String bairro,

        @NotBlank(message = "Cidade é obrigatória")
        @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
        String cidade,

        @NotBlank(message = "Estado é obrigatório")
        @Size(min = 2, max = 2, message = "Estado deve ter 2 caracteres (UF)")
        String estado,

        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP inválido")
        String cep

) {
	
	public Endereco toModel() {
		return new Endereco(this.logradouro,
				this.numero,
				this.complemento,
				this.bairro,
				this.cidade,
				this.estado,
				this.cep);
	}
}
