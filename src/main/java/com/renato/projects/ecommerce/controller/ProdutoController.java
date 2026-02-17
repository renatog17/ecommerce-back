package com.renato.projects.ecommerce.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renato.projects.ecommerce.service.ProdutoService;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

	private ProdutoService service;
	
	public ProdutoController(ProdutoService service) {
		super();
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<?> getProdutos(@PageableDefault(size = 20, sort="id") Pageable pageable){
		return ResponseEntity.ok(service.getAll(pageable));		
	}
}
