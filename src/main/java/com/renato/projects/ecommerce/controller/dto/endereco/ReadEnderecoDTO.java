package com.renato.projects.ecommerce.controller.dto.endereco;

import com.renato.projects.ecommerce.domain.Endereco;

public record ReadEnderecoDTO(
		Long id,
		String logradouro,
		String numero,
		String complemento,
		String bairro,
		String cidade,
		String estado,
		String cep) {

	public ReadEnderecoDTO(Endereco endereco) {
        this(
            endereco.getId(),
            endereco.getLogradouro(),
            endereco.getNumero(),
            endereco.getComplemento(),
            endereco.getBairro(),
            endereco.getCidade(),
            endereco.getEstado(),
            endereco.getCep()
        );
    }
}
