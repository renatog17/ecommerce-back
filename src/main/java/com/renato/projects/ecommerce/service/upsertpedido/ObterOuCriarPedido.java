package com.renato.projects.ecommerce.service.upsertpedido;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.PedidoRepository;
import com.renato.projects.ecommerce.service.ClienteService;

@Component
public class ObterOuCriarPedido {

	private ClienteService clienteService;
	private PedidoRepository pedidoRepository;
	
	public ObterOuCriarPedido(ClienteService clienteService, PedidoRepository pedidoRepository) {
		super();
		this.clienteService = clienteService;
		this.pedidoRepository = pedidoRepository;
	}

	public Pedido obterOuCriarPedido() {
		Cliente cliente = clienteService.buscarClienteAuthenticated();
		Optional<Pedido> pedido = pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO);
		if(pedido.isPresent())
			return pedido.get();
		else {
			Pedido novoPedido = new Pedido();
			novoPedido.setCliente(cliente);
			novoPedido.setDataIniciacao(LocalDate.now());
			novoPedido.setStatus(Status.INICIADO);
			novoPedido.setValorTotal(BigDecimal.ZERO);
			return novoPedido;
		}
		
	}
	
	
}
