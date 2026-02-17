package com.renato.projects.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.renato.projects.ecommerce.domain.Endereco;

public interface EnderecoRepository extends JpaRepository<Endereco, Long>{
	
	List<Endereco> findByClienteId(Long id);
}
