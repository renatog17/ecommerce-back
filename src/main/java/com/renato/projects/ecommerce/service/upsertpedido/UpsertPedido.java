package com.renato.projects.ecommerce.service.upsertpedido;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.domain.ProdutoPedido;
import com.renato.projects.ecommerce.repository.PedidoRepository;

@Component
public class UpsertPedido {

	private MapearItensPedidos mapearItensPedidos;
	private ObterOuCriarPedido obterOuCriarPedido;
	private PedidoRepository pedidoRepository;

	public UpsertPedido(MapearItensPedidos mapearItensPedidos, ObterOuCriarPedido obterOuCriarPedido,
			PedidoRepository pedidoRepository) {
		super();
		this.mapearItensPedidos = mapearItensPedidos;
		this.obterOuCriarPedido = obterOuCriarPedido;
		this.pedidoRepository = pedidoRepository;
	}
	
	public Pedido upsertPedido(PostPedidoDTO dto) {
		Map<Produto, Long> map = mapearItensPedidos.toMap(dto);
		Pedido pedido = obterOuCriarPedido.obterOuCriarPedido();
		List<ProdutoPedido> produtosPedidos = pedido.getProdutosPedidos();
		//1º incrementa a nova quantidade de produtos se este já existir no carrinho.
		for (ProdutoPedido produtoPedido : produtosPedidos) {
			if(map.containsKey(produtoPedido.getProduto())) {
				Long qtd = map.remove(produtoPedido.getProduto());
				produtoPedido.setQuantidade(produtoPedido.getQuantidade()+qtd);
			}
		}		
		//Instancia os novos produtos pedidos caso não existam
		List<ProdutoPedido> pps = new ArrayList<>();
		while (!map.isEmpty()) {
		    // pega a primeira entrada do map
		    Map.Entry<Produto, Long> entry = map.entrySet().iterator().next();

		    Produto produto = entry.getKey();
		    Long quantidade = entry.getValue();

		    // cria o ProdutoPedido
		    ProdutoPedido pp = new ProdutoPedido();
		    pp.setPedido(pedido);
		    pp.setProduto(produto);
		    pp.setQuantidade(quantidade);
		    pp.setValorUnitario(produto.getPreco());

			// adiciona à lista de itens do pedido
		    pps.add(pp);

		    // remove do map para evitar loop infinito
		    map.remove(produto);
		}
		
		if (pedido.getProdutosPedidos() == null) {
		    pedido.setProdutosPedidos(new ArrayList<>());
		}

		// adiciona todos os novos itens à lista existente
		pedido.getProdutosPedidos().addAll(pps);
		pedidoRepository.save(pedido);
		return pedido;
	}

	
}
