package com.renato.projects.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.renato.projects.ecommerce.controller.dto.produto.ReadProdutoDTO;
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
}
