package com.renato.projects.ecommerce.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.projects.ecommerce.controller.dto.categoria.PostCategoriaDTO;
import com.renato.projects.ecommerce.controller.dto.categoria.ReadCategoriaDTO;
import com.renato.projects.ecommerce.service.CategoriaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

	private CategoriaService categoriaService;

	public CategoriaController(CategoriaService categoriaService) {
		super();
		this.categoriaService = categoriaService;
	}
	
	@PostMapping
	public ResponseEntity<?> postCategoria(@Valid @RequestBody PostCategoriaDTO dto, UriComponentsBuilder uriBuilder){
		ReadCategoriaDTO readCategoriaDTO = categoriaService.postCategoria(dto);
		URI uri = uriBuilder.path("/categorias/{id}").buildAndExpand(readCategoriaDTO.id()).toUri();
		return ResponseEntity.created(uri).body(readCategoriaDTO);
	}
	
	@GetMapping
	public ResponseEntity<?> findAll(){
		return ResponseEntity.ok(categoriaService.findAll());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long id){
		categoriaService.delete(id);
		return ResponseEntity.noContent().build();
	}
}
