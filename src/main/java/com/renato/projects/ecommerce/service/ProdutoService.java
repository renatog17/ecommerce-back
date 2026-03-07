package com.renato.projects.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.produto.PostProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.ReadProdutoDTO;
import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.repository.CategoriaRepository;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@Service
public class ProdutoService {

	private ProdutoRepository produtoRepository;
	private CategoriaRepository categoriaRepository;
	
	
	public ProdutoService(ProdutoRepository produtoRepository) {
		super();
		this.produtoRepository = produtoRepository;
	}

	public Page<ReadProdutoDTO> getAll(Pageable pageable) {

		return produtoRepository.findAll(pageable).map(ReadProdutoDTO::new);
	}

	public ReadProdutoDTO save(PostProdutoDTO dto) {
		
		Categoria categoria = categoriaRepository.findById(dto.idCategoria())
		        .orElseThrow(() -> new ResponseStatusException(
		                HttpStatus.NOT_FOUND,
		                "Categoria não encontrada"
		        ));
		
		Produto produto = new Produto();
		produto.setCategoria(categoria);
		produto.setNome(dto.nome());
		produto.setDescricao(dto.descricao());
		produto.setPreco(dto.preco());
		produto.setQuantidade(dto.quantidade());
		
		return new ReadProdutoDTO(produto);
	}
	
	public Produto findProdutoById(Long id) {
		return produtoRepository.findByIdAndAtivoTrue(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
	}
	

	@Transactional
	public void delete(Long id) {
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		produto.setAtivo(false);
	}
}
