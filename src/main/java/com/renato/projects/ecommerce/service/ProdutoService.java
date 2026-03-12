package com.renato.projects.ecommerce.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.produto.AlterarPrecoProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.PatchProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.PostProdutoDTO;
import com.renato.projects.ecommerce.controller.dto.produto.ReadProdutoDTO;
import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.repository.CategoriaRepository;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

import jakarta.validation.Valid;

@Service
public class ProdutoService {

	private ProdutoRepository produtoRepository;
	private CategoriaRepository categoriaRepository;
	
	
	public ProdutoService(ProdutoRepository produtoRepository, CategoriaRepository categoriaRepository) {
		super();
		this.produtoRepository = produtoRepository;
		this.categoriaRepository = categoriaRepository;
	}
	

	public Page<ReadProdutoDTO> getAll(Long categoriaId, Pageable pageable) {
		
		if(categoriaId==null)
			return produtoRepository.findAllByAtivoTrue(pageable).map(ReadProdutoDTO::new);
		else
			return produtoRepository.findAllByCategoriaIdAndAtivoTrue(categoriaId, pageable).map(ReadProdutoDTO::new);
	}

	@Transactional
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
		produtoRepository.save(produto);
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

	@Transactional
	public ReadProdutoDTO editProduto(Long id, @Valid PatchProdutoDTO dto) {

	    Produto produto = produtoRepository.findById(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));

	    if (dto.nome() != null) {
	        produto.setNome(dto.nome());
	    }

	    if (dto.quantidade() != null) {
	        produto.setQuantidade(dto.quantidade());
	    }

	    if (dto.preco() != null) {
	        produto.setPreco(dto.preco());
	    }

	    if (dto.idCategoria() != null) {
	        Categoria categoria = categoriaRepository.findById(dto.idCategoria())
	                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

	        produto.setCategoria(categoria);
	    }

	    return new ReadProdutoDTO(produto);
	}

	public ReadProdutoDTO get(Long id) {
		return new ReadProdutoDTO(produtoRepository.findByIdAndAtivoTrue(id)
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado")));
	}

	@Transactional
	public void alterarPrecoProduto(Long id, AlterarPrecoProdutoDTO dto) {
		
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		produto.setPreco(dto.novoPreco());
	}
}
