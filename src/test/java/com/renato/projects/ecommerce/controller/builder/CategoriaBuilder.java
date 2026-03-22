package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Categoria;

public class CategoriaBuilder {

    private Categoria categoria;

    public CategoriaBuilder() {
        categoria = new Categoria();
        categoria.setNome("Categoria padrão");
        categoria.setDescricao("Descrição padrão");
        categoria.setAtivo(true);
    }

    public CategoriaBuilder comNome(String nome) {
        categoria.setNome(nome);
        return this;
    }

    public CategoriaBuilder comDescricao(String descricao) {
        categoria.setDescricao(descricao);
        return this;
    }

    public CategoriaBuilder inativa() {
        categoria.setAtivo(false);
        return this;
    }

    public Categoria build() {
        return categoria;
    }
}