package com.renato.projects.ecommerce.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.pedido.ItemPedidoDTO;
import com.renato.projects.ecommerce.controller.dto.pedido.ReadPedidoDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.domain.ProdutoPedido;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.PedidoRepository;
import com.renato.projects.ecommerce.repository.ProdutoPedidoRepository;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	private AuthenticatedUserService authenticatedUserService;
	private ProdutoService produtoService;
	private ProdutoPedidoRepository produtoPedidoRepository;
	
	public PedidoService(PedidoRepository pedidoRepository,
			AuthenticatedUserService authenticatedUserService,
			ProdutoService produtoService,
			ProdutoPedidoRepository produtoPedidoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.authenticatedUserService = authenticatedUserService;
		this.produtoService = produtoService;
		this.produtoPedidoRepository = produtoPedidoRepository;
	}


	public ReadPedidoDTO getCart() {
		UserDetailsImpl user = authenticatedUserService.getUsuario();
		Optional<Pedido> pedido = pedidoRepository.findCarrinhoByUserId(user.getId());
		return new ReadPedidoDTO(pedido.get());
	}
	
	@Transactional
	public ReadPedidoDTO addItem(ItemPedidoDTO dto) {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
		Produto produto = produtoService.findProdutoById(dto.idProduto());
		
		List<ProdutoPedido> produtosPedido = produto.getProdutoPedido();
		
		ProdutoPedido pp = null;
		
		for (ProdutoPedido produtoPedido : produtosPedido) {
			if(produtoPedido.getId() == dto.idProduto()) {
				pp = produtoPedido;
				break;
			}
		}
		if(pp == null) {
			pp = new ProdutoPedido();
			produto.getProdutoPedido().add(pp);
		}
		
		Long qtdASerComprada = produtoService.qtdDisponivel(dto.qtd(), produto.getQuantidade());
		
		pp.setProduto(produto);
		pp.setQuantidade(pp.getQuantidade() + qtdASerComprada);
		pp.setPedido(pedido);
		produtoPedidoRepository.save(pp);
		
		return new ReadPedidoDTO(pedido);
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
		
		Pedido novoPedido = new Pedido();
		novoPedido.setStatus(Status.INICIADO);
		pedidoRepository.save(novoPedido);
	}

	public List<ReadPedidoDTO> obterPedidosPorClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
		return pedidos.stream().map(ReadPedidoDTO::new).toList();
	}
}

