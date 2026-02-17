package com.renato.projects.ecommerce.controller.dto.cliente;

import java.time.LocalDate;
import java.util.List;

import com.renato.projects.ecommerce.controller.dto.endereco.PostEnderecoDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Endereco;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record PostClienteDTO(@NotBlank String nome, 
		@NotBlank String telefone, 
		@NotBlank @Size(max = 11) String cpf,
		@NotNull PostEnderecoDTO endereco) {

	public Cliente toModel() {

		Cliente cliente = new Cliente();
		cliente.setNome(this.nome());
		cliente.setTelefone(this.telefone());
		cliente.setCpf(this.cpf());
		cliente.setDataCadastro(LocalDate.now());

		Endereco enderecoModel = this.endereco().toModel();
		enderecoModel.setCliente(cliente);

		cliente.setEndereco(List.of(enderecoModel));

		return cliente;
	}

}
