package com.renato.projects.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.produto.ReadProdutoDTO;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository repo;

	public ProdutoService(ProdutoRepository repo) {
		super();
		this.repo = repo;
	}

	public Page<ReadProdutoDTO> getAll(Pageable pageable) {

		return repo.findAll(pageable).map(ReadProdutoDTO::new);
	}

	public Produto findProdutoById(Long id) {
		return repo.findByIdAndAtivoTrue(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
	}
	
	public Long qtdDisponivel(Long qtdSolicitada, Long qtdDisponivel) {
		if(qtdSolicitada>= qtdDisponivel) {
			return qtdSolicitada;
		}else {
			return qtdDisponivel;
		}
	}
}
