package com.renato.projects.ecommerce.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.projects.ecommerce.controller.dto.produto.PostProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.ReadProdutoDTO;
import com.renato.projects.ecommerce.service.ProdutoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private ProdutoService service;
	
	public ProdutoController(ProdutoService service) {
		super();
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> postProdutos(@Valid @RequestBody PostProdutoDTO dto, UriComponentsBuilder uriBuilder){
		ReadProdutoDTO readProduto = service.save(dto);
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(readProduto.id()).toUri();
		return ResponseEntity.created(uri).body(readProduto);
	}
	
	@GetMapping
	public ResponseEntity<?> getProdutos(@PageableDefault(size = 20, sort="id") Pageable pageable){
		return ResponseEntity.ok(service.getAll(pageable));		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
