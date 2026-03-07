package com.renato.projects.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.categoria.PostCategoriaDTO;
import com.renato.projects.ecommerce.controller.dto.categoria.ReadCategoriaDTO;
import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {

	private CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		super();
		this.categoriaRepository = categoriaRepository;
	}

	public ReadCategoriaDTO postCategoria(PostCategoriaDTO dto) {
		Categoria categoria = new Categoria();
		categoria.setAtivo(true);
		categoria.setNome(dto.nome());
		categoria.setDescricao(dto.descricao());
		categoriaRepository.save(categoria);
		return new ReadCategoriaDTO(categoria);
	}
	
	public List<ReadCategoriaDTO> findAll() {
		return categoriaRepository.findAll().stream().map(ReadCategoriaDTO::new).collect(Collectors.toList());
	}
	
	@Transactional
	public void delete(Long id) {
		Categoria categoria = categoriaRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		categoria.setAtivo(false);
	}

}
