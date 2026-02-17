package com.renato.projects.ecommerce.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.controller.dto.pedido.ReadPedidoDTO;
import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.domain.ProdutoPedido;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.PedidoRepository;
import com.renato.projects.ecommerce.repository.ProdutoPedidoRepository;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	private ProdutoRepository produtoRepository;
	private AuthenticatedUserService authenticatedUserService;
	private ProdutoPedidoRepository produtoPedidoRepository;
	
	public PedidoService(PedidoRepository pedidoRepository,ProdutoRepository produtoRepository,
			AuthenticatedUserService authenticatedUserService, ProdutoPedidoRepository produtoPedidoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.produtoRepository = produtoRepository;
		this.authenticatedUserService = authenticatedUserService;
		this.produtoPedidoRepository = produtoPedidoRepository;
	}
	
	@Transactional
	public Pedido criarPedido(PostPedidoDTO dto) {
		Map<Long, Long> produtosSolicitados = new HashMap<Long, Long>();
		
		for(int i = 0; i<dto.itens().size(); i++) {
			produtosSolicitados.put(dto.itens().get(i).idProduto(), dto.itens().get(i).qtd());
		}
		
		List<Long> ids = new ArrayList<>(produtosSolicitados.keySet());
		List<Produto> produtos = produtoRepository.findAllById(ids);
		
		Pedido pedido = new Pedido();
		pedido.setStatus(Status.INICIADO);
		pedido.setCliente(authenticatedUserService.getUsuario().getCliente());
		//pode ser que dê erro na linha acima por cliente não estar carregado
		
		List<ProdutoPedido> pps = new ArrayList<ProdutoPedido>();
		for (Produto produto : produtos) {
			ProdutoPedido pp = new ProdutoPedido();
			pp.setPedido(pedido);
			pp.setProduto(produto);
			pp.setQuantidade(produtosSolicitados.get(produto.getId()));
			//verificar disso acima se tornar um null
			pp.setValorUnitario(produto.getPreco());
			pps.add(pp);
		}
		
		pedidoRepository.save(pedido);
		produtoPedidoRepository.saveAll(pps);
		return pedido;
	}

	public void finalizarPedido(Long id) {
		Pedido pedido = pedidoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse pedido não existe"));
		pedido.setStatus(Status.CONCLUIDO);
		
	}

	public ReadPedidoDTO obterPedidoComStatusIniciadoDoClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatusIniciado(cliente)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
		return new ReadPedidoDTO(pedido);
	}

	public List<ReadPedidoDTO> obterPedidosPorClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
		return pedidos.stream().map(ReadPedidoDTO::new).toList();
	}
}

