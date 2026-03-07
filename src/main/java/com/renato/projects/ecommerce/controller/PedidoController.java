package com.renato.projects.ecommerce.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@PatchMapping("/finalize")
	public ResponseEntity<?> finalizarPedido(){
		ReadPedidoDTO pedidoFinalizado = pedidoService.finalizarPedido();
		return ResponseEntity.ok(pedidoFinalizado);
	}
	
	@GetMapping("/cart")
	public ResponseEntity<?> getCart(){
		return ResponseEntity.ok(pedidoService.getPedidoComStatusIniciado());
	}
	
	@GetMapping()
	public ResponseEntity<?> obterPedidosPorClienteAutenticado(){
		return ResponseEntity.ok(pedidoService.obterPedidosPorClienteAutenticado());
	}
	
	@DeleteMapping("/itens/{id}")
	public ResponseEntity<?> removerItemPedido(@PathVariable Long id){
		pedidoService.removerItemPedido(id);
		return ResponseEntity.noContent().build();
	}
	
	@PutMapping("/itens/{id}")
	public ResponseEntity<?> alterarQuantidadeItemPedido(@PathVariable Long id, Long quantidadeDelta ){
		ReadPedidoDTO pedidoDTO = pedidoService.alterarQuantidadeItemPedido(id, quantidadeDelta);
		return ResponseEntity.ok(pedidoDTO);
	}
}
