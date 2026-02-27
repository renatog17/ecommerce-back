package com.renato.projects.ecommerce.service.upsertpedido;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@Component
public class MapearItensPedidos {

    private final ProdutoRepository produtoRepository;

    public MapearItensPedidos(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Map<Produto, Long> toMap(PostPedidoDTO dto) {

        // 1️ Map produtoId -> quantidade
        Map<Long, Long> produtosSolicitados = new HashMap<>();

        for (var item : dto.itens()) {
            produtosSolicitados.put(item.idProduto(), item.qtd());
        }

        // 2️ Buscar produtos no banco
        List<Long> ids = new ArrayList<>(produtosSolicitados.keySet());
        List<Produto> produtos = produtoRepository.findAllById(ids);

        // 3 Criar Map<Produto, Quantidade>
        Map<Produto, Long> resultado = new HashMap<>();

        for (Produto produto : produtos) {
            Long quantidade = produtosSolicitados.get(produto.getId());
            resultado.put(produto, quantidade);
        }

        return resultado;
    }
}