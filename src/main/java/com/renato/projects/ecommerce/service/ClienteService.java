package com.renato.projects.ecommerce.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.cliente.PutClienteDTO;
import com.renato.projects.ecommerce.controller.dto.cliente.ReadClienteDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.repository.ClienteRepository;

@Service
public class ClienteService {

	private AuthenticatedUserService authenticatedUserService;
	private ClienteRepository clienteRepository;

	public ClienteService(ClienteRepository clienteRepository, AuthenticatedUserService authenticatedUserService) {
		super();
		this.clienteRepository = clienteRepository;
		this.authenticatedUserService = authenticatedUserService;
	}

	@Transactional
	public Cliente salvarCliente (Cliente cliente) {
		return clienteRepository.save(cliente);
	}
	
	public ReadClienteDTO buscarCliente(Long id) {
		
		Cliente cliente = clienteRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		
		return new ReadClienteDTO(cliente);
	}
	
	public void atualizarCliente (PutClienteDTO putClienteDTO) {
		Cliente cliente = clienteRepository.findById(putClienteDTO.id())
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		if(putClienteDTO.nome()!=null) 
			cliente.setNome(putClienteDTO.nome());
		if(putClienteDTO.telefone()!=null)
			cliente.setTelefone(putClienteDTO.telefone());
	}
	
	@Transactional
	public void deletarCliente(Long id) {
		Cliente cliente = clienteRepository.findById(id)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		cliente.setAtivo(false);
	}

	public Cliente buscarClienteAuthenticated() {
		UserDetailsImpl usuario = authenticatedUserService.getUsuario();
		Cliente cliente = clienteRepository.findByUser(usuario)
			.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
		return cliente;
	}
}
