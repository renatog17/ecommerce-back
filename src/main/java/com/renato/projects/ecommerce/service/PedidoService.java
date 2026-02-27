package com.renato.projects.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.controller.dto.pedido.ReadPedidoDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.ProdutoPedido;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.PedidoRepository;
import com.renato.projects.ecommerce.service.upsertpedido.UpsertPedido;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	private AuthenticatedUserService authenticatedUserService;
	private UpsertPedido upsertPedido;
	
	public PedidoService(PedidoRepository pedidoRepository,
			AuthenticatedUserService authenticatedUserService,
			UpsertPedido upsertPedido) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.authenticatedUserService = authenticatedUserService;
		this.upsertPedido = upsertPedido;
	}
	
	@Transactional
	public ReadPedidoDTO criarPedido(PostPedidoDTO dto) {
		
		return new ReadPedidoDTO(upsertPedido.upsertPedido(dto));
	}

	@Transactional
	public void finalizarPedido(Long id) {
		
		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse pedido não existe"));
		pedido.setStatus(Status.CONCLUIDO);
		pedido.setDataCriacao(LocalDate.now());
		List<ProdutoPedido> produtosPedidos = pedido.getProdutosPedidos();
		BigDecimal valorTotal = BigDecimal.ZERO;
		for (ProdutoPedido produtoPedido : produtosPedidos) {
			valorTotal = valorTotal.add(produtoPedido.getValorUnitario());
		}
		pedido.setValorTotal(valorTotal);
		
	}

	public ReadPedidoDTO obterPedidoComStatusIniciadoDoClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
		return new ReadPedidoDTO(pedido);
	}

	public List<ReadPedidoDTO> obterPedidosPorClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
		return pedidos.stream().map(ReadPedidoDTO::new).toList();
	}
}

