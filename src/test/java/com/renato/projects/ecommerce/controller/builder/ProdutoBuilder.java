package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.domain.Produto;

import java.math.BigDecimal;

public class ProdutoBuilder {

    private Produto produto;

    public ProdutoBuilder() {
        produto = new Produto();
        produto.setNome("Produto padrão");
        produto.setDescricao("Descrição padrão");
        produto.setQuantidade(10L);
        produto.setPreco(BigDecimal.valueOf(100));
    }

    public ProdutoBuilder comNome(String nome) {
        produto.setNome(nome);
        return this;
    }

    public ProdutoBuilder comPreco(BigDecimal preco) {
        produto.setPreco(preco);
        return this;
    }

    public ProdutoBuilder comCategoria(Categoria categoria) {
        produto.setCategoria(categoria);
        return this;
    }

    public Produto build() {
        return produto;
    }
}