package com.renato.projects.ecommerce.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.renato.projects.ecommerce.controller.dto.produto.AlterarPrecoProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.PatchProdutoDTO;
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
	public ResponseEntity<?> postProduto(@Valid @RequestBody PostProdutoDTO dto, UriComponentsBuilder uriBuilder){
		ReadProdutoDTO readProduto = service.save(dto);
		URI uri = uriBuilder.path("/produtos/{id}").buildAndExpand(readProduto.id()).toUri();
		return ResponseEntity.created(uri).body(readProduto);
	}
	
	@GetMapping
	public ResponseEntity<?> getProdutos(
	        @RequestParam(required = false) Long categoriaId,
	        @PageableDefault(size = 20, sort = "id") Pageable pageable) {

	    return ResponseEntity.ok(service.getAll(categoriaId, pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getProduto(@PathVariable Long id){
		return ResponseEntity.ok(service.get(id));		
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<?> editarProduto(@Valid @RequestBody PatchProdutoDTO dto, @PathVariable Long id){
		return ResponseEntity.ok(service.editProduto(id, dto));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteProduto(@PathVariable Long id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PatchMapping("/{id}/alterar-preco")
	public ResponseEntity<?> alterarPreco(@PathVariable Long id, @Valid AlterarPrecoProdutoDTO dto) {
		service.alterarPrecoProduto(id, dto);
		return ResponseEntity.ok().build();
	}
}
