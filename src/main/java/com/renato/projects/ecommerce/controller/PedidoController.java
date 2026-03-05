package com.renato.projects.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.renato.projects.ecommerce.controller.dto.pedido.ItemPedidoDTO;
import com.renato.projects.ecommerce.controller.dto.pedido.ReadPedidoDTO;
import com.renato.projects.ecommerce.service.PedidoService;


@RestController
@RequestMapping("/pedidos")
public class PedidoController {

	private PedidoService pedidoService;

	public PedidoController(PedidoService pedidoService) {
		super();
		this.pedidoService = pedidoService;
	}	
	
	@PatchMapping("/cart")
	public ResponseEntity<?> addItem(@RequestBody ItemPedidoDTO dto) {
		
		  	ReadPedidoDTO pedido = pedidoService.addItem(dto);
		    return ResponseEntity.ok(pedido);
	}
	
	@PatchMapping("/{id}/finalize")
	public ResponseEntity<?> finalizarPedido(@PathVariable Long id){
		pedidoService.finalizarPedido(id);
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/cart")
	public ResponseEntity<?> getCart(){
		return ResponseEntity.ok(pedidoService.getCart());
	}
	
	@GetMapping()
	public ResponseEntity<?> obterPedidosPorClienteAutenticado(){
		return ResponseEntity.ok(pedidoService.obterPedidosPorClienteAutenticado());
	}
	
}
