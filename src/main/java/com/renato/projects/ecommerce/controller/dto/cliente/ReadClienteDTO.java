package com.renato.projects.ecommerce.controller.dto.cliente;

import java.util.List;

import com.renato.projects.ecommerce.controller.dto.endereco.ReadEnderecoDTO;
import com.renato.projects.ecommerce.domain.Cliente;

public record ReadClienteDTO(
		Long id,
		String nome,
		String telefone,
		String cpf,
		List<ReadEnderecoDTO> endereco) {
		
	public ReadClienteDTO(Cliente cliente) {
	    this(
	        cliente.getId(),
	        cliente.getNome(),
	        cliente.getTelefone(),
	        cliente.getCpf(),
	        cliente.getEndereco() != null
	            ? cliente.getEndereco()
	                .stream()
	                .map(ReadEnderecoDTO::new)
	                .toList()
	            : List.of()
	    );
	}

}
