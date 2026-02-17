package com.renato.projects.ecommerce.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renato.projects.ecommerce.controller.dto.pedido.PostPedidoDTO;
import com.renato.projects.ecommerce.service.PedidoService;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}	
	
	@PostMapping
	public ResponseEntity<?> postPedido(@RequestBody PostPedidoDTO dto) {
		pedidoService.criarPedido(dto);
		return ResponseEntity.ok().build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> finalizarPedido(@PathVariable Long id){
		pedidoService.finalizarPedido(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/iniciado")
	public ResponseEntity<?> obterPedidoComStatusIniciadoDoClienteAutenticado(){
		return ResponseEntity.ok(pedidoService.obterPedidoComStatusIniciadoDoClienteAutenticado());
	}
	
	@GetMapping()
	public ResponseEntity<?> obterPedidosPorClienteAutenticado(){
		return ResponseEntity.ok(pedidoService.obterPedidosPorClienteAutenticado());
	}
	
}
