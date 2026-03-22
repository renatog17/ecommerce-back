package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Endereco;

public class EnderecoBuilder {

    private Endereco endereco;

    public EnderecoBuilder() {
        endereco = new Endereco();
        endereco.setLogradouro("Rua A");
        endereco.setNumero("123");
        endereco.setBairro("Centro");
        endereco.setCidade("Salvador");
        endereco.setEstado("BA");
        endereco.setCep("40000000");
        endereco.setAtivo(true);
    }

    public EnderecoBuilder comCliente(Cliente cliente) {
        endereco.setCliente(cliente);
        return this;
    }

    public EnderecoBuilder comCidade(String cidade) {
        endereco.setCidade(cidade);
        return this;
    }

    public Endereco build() {
        return endereco;
    }
}