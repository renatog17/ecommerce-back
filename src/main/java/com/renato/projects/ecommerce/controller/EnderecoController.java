package com.renato.projects.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renato.projects.ecommerce.controller.dto.endereco.PostEnderecoDTO;
import com.renato.projects.ecommerce.controller.dto.endereco.PutEnderecoDTO;
import com.renato.projects.ecommerce.service.EnderecoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {

	private EnderecoService enderecoService;
	
	public EnderecoController(EnderecoService enderecoService) {
		super();
		this.enderecoService = enderecoService;
	}

	@PostMapping
	public ResponseEntity<?> salvarEndereco(@Valid @RequestBody PostEnderecoDTO postEnderecoDTO) {
		enderecoService.salvarEndereco(postEnderecoDTO);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping
	public ResponseEntity<?> atualizarEndereco(@Valid @RequestBody PutEnderecoDTO putEnderecoDTO) {
		enderecoService.atualizarEndereco(putEnderecoDTO);
		return ResponseEntity.ok().build();
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletarEndereco(@PathVariable Long id){
		enderecoService.deletarPorId(id);
		return ResponseEntity.noContent().build();
	}
}
