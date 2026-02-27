package com.renato.projects.ecommerce.service.upsertpedido;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.renato.projects.ecommerce.domain.Cliente;
import com.renato.projects.ecommerce.domain.Pedido;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.PedidoRepository;
import com.renato.projects.ecommerce.service.ClienteService;

@ExtendWith(MockitoExtension.class)
class ObterOuCriarPedidoTest {

    @Mock
    private ClienteService clienteService;

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private ObterOuCriarPedido obterOuCriarPedido;

    @Test
    void deveRetornarPedidoExistente() {
        // Arrange
        Cliente cliente = new Cliente();
        Pedido pedidoExistente = new Pedido();

        when(clienteService.buscarClienteAuthenticated())
                .thenReturn(cliente);

        when(pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO))
                .thenReturn(Optional.of(pedidoExistente));

        // Act
        Pedido resultado = obterOuCriarPedido.obterOuCriarPedido();

        // Assert
        assertThat(resultado).isEqualTo(pedidoExistente);

        verify(clienteService).buscarClienteAuthenticated();
        verify(pedidoRepository)
                .findByClienteAndStatus(cliente, Status.INICIADO);
    }

    @Test
    void deveCriarNovoPedidoSeNaoExistir() {
        // Arrange
        Cliente cliente = new Cliente();

        when(clienteService.buscarClienteAuthenticated())
                .thenReturn(cliente);

        when(pedidoRepository.findByClienteAndStatus(cliente, Status.INICIADO))
                .thenReturn(Optional.empty());

        // Act
        Pedido resultado = obterOuCriarPedido.obterOuCriarPedido();

        // Assert
        assertThat(resultado).isNotNull();
        assertThat(resultado.getCliente()).isEqualTo(cliente);
        assertThat(resultado.getStatus()).isEqualTo(Status.INICIADO);
        assertThat(resultado.getValorTotal()).isEqualTo(BigDecimal.ZERO);
        assertThat(resultado.getDataIniciacao())
                .isEqualTo(LocalDate.now());

        verify(clienteService).buscarClienteAuthenticated();
        verify(pedidoRepository)
                .findByClienteAndStatus(cliente, Status.INICIADO);
    }
}