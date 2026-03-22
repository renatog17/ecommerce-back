package com.renato.projects.ecommerce.controller.builder;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PedidoBuilder {

    private Pedido pedido;

    public PedidoBuilder() {
        pedido = new Pedido();
        pedido.setDataCriacao(LocalDate.now());
        pedido.setStatus(Status.INICIADO); // ajuste conforme seu enum
        pedido.setValorTotal(BigDecimal.ZERO);
    }

    public PedidoBuilder comCliente(Cliente cliente) {
        pedido.setCliente(cliente);
        return this;
    }

    public PedidoBuilder comStatus(Status status) {
        pedido.setStatus(status);
        return this;
    }

    public PedidoBuilder comDataCriacao(LocalDate data) {
        pedido.setDataCriacao(data);
        return this;
    }

    public PedidoBuilder comValorTotal(BigDecimal valor) {
        pedido.setValorTotal(valor);
        return this;
    }

    public Pedido build() {
        return pedido;
    }
}