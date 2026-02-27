package com.renato.projects.ecommerce.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.projects.ecommerce.controller.dto.cliente.PostClienteDTO;
import com.renato.projects.ecommerce.controller.dto.cliente.PutClienteDTO;
import com.renato.projects.ecommerce.controller.dto.cliente.ReadClienteDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.service.ClienteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

	private ClienteService clienteService;

	public ClienteController(ClienteService clienteService) {
		super();
		this.clienteService = clienteService;
	}

	@PostMapping
	public ResponseEntity<?> cadastrarCliente(@Valid @RequestBody PostClienteDTO postClienteDTO, 
			UriComponentsBuilder uriComponentsBuilder){
		Cliente cliente = clienteService.salvarCliente(postClienteDTO.toModel());
		URI uri = uriComponentsBuilder.path("/tenant/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@GetMapping()
	public ResponseEntity<?> buscarClienteAuthenticated(){
		Cliente cliente = clienteService.buscarClienteAuthenticated();
		ReadClienteDTO readClienteDTO = new ReadClienteDTO(cliente);
		return ResponseEntity.ok(readClienteDTO);
	}
	
	@PutMapping
	public ResponseEntity<?> atualizarCliente(@RequestBody PutClienteDTO putClienteDTO){
		clienteService.atualizarCliente(putClienteDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarCliente(@PathVariable Long id){
		clienteService.deletarCliente(id);
		return ResponseEntity.noContent().build();
	}
}
