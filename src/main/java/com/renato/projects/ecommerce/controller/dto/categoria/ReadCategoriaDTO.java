package com.renato.projects.ecommerce.controller.dto.categoria;

import com.renato.projects.ecommerce.domain.Categoria;

public record ReadCategoriaDTO(
		Long id,
		String nome,
		String descricao) 
{
	public ReadCategoriaDTO(Categoria categoria) {
		this(
			categoria.getId(),
			categoria.getNome(),
			categoria.getDescricao()
		);
		
	}
}
