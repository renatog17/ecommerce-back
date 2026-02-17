package com.renato.projects.ecommerce.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.renato.projects.ecommerce.controller.dto.categoria.ReadCategoriaDTO;
import com.renato.projects.ecommerce.repository.CategoriaRepository;

@Service
public class CategoriaService {

	private CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		super();
		this.categoriaRepository = categoriaRepository;
	}

	public List<ReadCategoriaDTO> findAll() {
		return categoriaRepository.findAll().stream().map(ReadCategoriaDTO::new).collect(Collectors.toList());
	}

}
