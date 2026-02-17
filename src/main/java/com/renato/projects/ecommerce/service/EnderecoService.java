package com.renato.projects.ecommerce.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.endereco.PostEnderecoDTO;
import com.renato.projects.ecommerce.controller.dto.endereco.PutEnderecoDTO;
import com.renato.projects.ecommerce.controller.dto.endereco.ReadEnderecoDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Endereco;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.repository.ClienteRepository;
import com.renato.projects.ecommerce.repository.EnderecoRepository;

@Service
public class EnderecoService {

	private EnderecoRepository enderecoRepository;
	private AuthenticatedUserService authenticatedUserService;
	private ClienteRepository clienteRepository;

	public EnderecoService(EnderecoRepository enderecoRepository, ClienteRepository clienteRepository) {
		super();
		this.enderecoRepository = enderecoRepository;
		this.clienteRepository = clienteRepository;
	}
	
	@Transactional
	public void salvarEndereco(PostEnderecoDTO enderecoDTO) {

		UserDetailsImpl authenticatedUser = authenticatedUserService.getUsuario();

		Cliente cliente = clienteRepository.findByUserId(authenticatedUser.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

		Endereco endereco = enderecoDTO.toModel();

		endereco.setCliente(cliente);

		enderecoRepository.save(endereco);
	}
	
	public List<ReadEnderecoDTO> buscarEnderecosPorCliente(Long idCliente) {
		List<Endereco> clientes = enderecoRepository.findByClienteId(idCliente);
		return clientes.stream().map(ReadEnderecoDTO::new).toList();
	}

	@Transactional
	public void atualizarEndereco(PutEnderecoDTO putEnderecoDTO) {

		Endereco endereco = enderecoRepository.findById(putEnderecoDTO.id())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco não encontrado"));
		
		if (putEnderecoDTO.logradouro() != null) {
	        endereco.setLogradouro(putEnderecoDTO.logradouro());
	    }

	    if (putEnderecoDTO.numero() != null) {
	        endereco.setNumero(putEnderecoDTO.numero());
	    }

	    if (putEnderecoDTO.complemento() != null) {
	        endereco.setComplemento(putEnderecoDTO.complemento());
	    }

	    if (putEnderecoDTO.bairro() != null) {
	        endereco.setBairro(putEnderecoDTO.bairro());
	    }

	    if (putEnderecoDTO.cidade() != null) {
	        endereco.setCidade(putEnderecoDTO.cidade());
	    }

	    if (putEnderecoDTO.estado() != null) {
	        endereco.setEstado(putEnderecoDTO.estado());
	    }

	    if (putEnderecoDTO.cep() != null) {
	        endereco.setCep(putEnderecoDTO.cep());
	    }
	}
	
	@Transactional
	public void deletarPorId(Long id) {
		Endereco endereco = enderecoRepository.findById(id)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Endereco nao encontrado"));
		endereco.setAtivo(false);
	}
}
