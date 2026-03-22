package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;

import java.time.LocalDate;

public class ClienteBuilder {

    private Cliente cliente;

    public ClienteBuilder() {
        cliente = new Cliente();
        cliente.setNome("Cliente padrão");
        cliente.setTelefone("71999999999");
        cliente.setCpf("12345678900");
        cliente.setDataCadastro(LocalDate.now());
        cliente.setAtivo(true);
    }

    public ClienteBuilder comNome(String nome) {
        cliente.setNome(nome);
        return this;
    }

    public ClienteBuilder comCpf(String cpf) {
        cliente.setCpf(cpf);
        return this;
    }

    public ClienteBuilder comUser(UserDetailsImpl user) {
        cliente.setUser(user);
        return this;
    }

    public ClienteBuilder inativo() {
        cliente.setAtivo(false);
        return this;
    }

    public Cliente build() {
        return cliente;
    }
}