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
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@Service
public class PedidoService {
	
	private PedidoRepository pedidoRepository;
	private AuthenticatedUserService authenticatedUserService;
	private ProdutoService produtoService;
	private ProdutoPedidoRepository produtoPedidoRepository;
	private ProdutoRepository produtoRepository;
	
	public PedidoService(PedidoRepository pedidoRepository,
			AuthenticatedUserService authenticatedUserService,
			ProdutoService produtoService,
			ProdutoPedidoRepository produtoPedidoRepository,
			ProdutoRepository produtoRepository) {
		super();
		this.pedidoRepository = pedidoRepository;
		this.authenticatedUserService = authenticatedUserService;
		this.produtoService = produtoService;
		this.produtoPedidoRepository = produtoPedidoRepository;
		this.produtoRepository = produtoRepository;
	}


	public ReadPedidoDTO getPedidoComStatusIniciado() {
		UserDetailsImpl user = authenticatedUserService.getUsuario();
		Cliente cliente = user.getCliente();
		Optional<Pedido> pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO);
		return new ReadPedidoDTO(pedido.get());
	}
	
	@Transactional
	public ReadPedidoDTO addItem(ItemPedidoDTO dto) {
		//obter pedido{
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		//}
		//obter produto{
		Produto produto = produtoService.findProdutoById(dto.idProduto());
		//}
		//buscar item pedido por pedido e produto
		Optional<ProdutoPedido> optionalPP = produtoPedidoRepository.findByProdutoIdAndPedidoId(produto.getId(), pedido.getId());
		
		//buscar ou criar um item pedido{
		ProdutoPedido pp = null;
		if(optionalPP.isPresent()) {
			pp = optionalPP.get();
		}else {
			pp = new ProdutoPedido();
			produto.getProdutoPedido().add(pp);
			pp.setQuantidade(0L);
		}
		//}
		
		if(dto.qtd() > produto.getQuantidade())
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade disponível mudou");
		
		Long qtdASerComprada = Math.max(dto.qtd(), produto.getQuantidade());
		pp.setProduto(produto);
		pp.setPedido(pedido);
		pp.setQuantidade(pp.getQuantidade() + qtdASerComprada);
		pp.setValorUnitario(produto.getPreco());
		produtoPedidoRepository.save(pp);
		return new ReadPedidoDTO(pedido);
	}
	
	@Transactional
	public ReadPedidoDTO finalizarPedido() {
		UserDetailsImpl usuario = authenticatedUserService.getUsuario();
		Pedido pedido = pedidoRepository.findCarrinhoByUserId(usuario.getId()).get();

		if(pedido.getProdutosPedidos().size() == 0)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O carrinho está vazio");
		
		List<ProdutoPedido> cart = pedido.getProdutosPedidos();
		
		BigDecimal valorTotal = BigDecimal.ZERO;
		
		for (ProdutoPedido produtoPedido : cart) {
			if(produtoPedido.getProduto().getQuantidade() < produtoPedido.getQuantidade()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A quantidade disponível de algum produto mudou");
			}
			Produto produto = produtoPedido.getProduto();
			produto.setQuantidade(produto.getQuantidade() - produtoPedido.getQuantidade());
			valorTotal = valorTotal.add(produtoPedido.getValorUnitario().multiply(BigDecimal.valueOf(produtoPedido.getQuantidade())));			
		}
		pedido.setValorTotal(valorTotal);
		pedido.setDataCriacao(LocalDate.now());
		pedido.setStatus(Status.CONCLUIDO);
		
		Pedido novoPedido = new Pedido();
		novoPedido.setStatus(Status.INICIADO);
		pedidoRepository.save(novoPedido);
		return new ReadPedidoDTO(pedido);
	}

	public List<ReadPedidoDTO> obterPedidosPorClienteAutenticado() {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		List<Pedido> pedidos = pedidoRepository.findByCliente(cliente);
		return pedidos.stream().map(ReadPedidoDTO::new).toList();
	}


	@Transactional
	public void removerItemPedido(Long id) {
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
		Produto produto = produtoRepository.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		
		produtoPedidoRepository.deleteByProdutoIdAndPedidoId(produto.getId(), pedido.getId());
	}

	@Transactional
	public ReadPedidoDTO alterarQuantidadeItemPedido(Long idProduto, Long quantidadeDelta) {
		
		Cliente cliente = authenticatedUserService.getUsuario().getCliente();
		Pedido pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO)
				.orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido não encontrado"));
		
		Produto produto = produtoRepository.findById(idProduto)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
		
		ProdutoPedido pp = produtoPedidoRepository.findByProdutoIdAndPedidoId(produto.getId(), pedido.getId())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Item pedido não encontrado"));
		
		pp.setQuantidade(pp.getQuantidade() + quantidadeDelta);
		
		
		return new ReadPedidoDTO(pedido);
	}
}

