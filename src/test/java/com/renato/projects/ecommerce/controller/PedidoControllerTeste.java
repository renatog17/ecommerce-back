package com.renato.projects.ecommerce.controller;

import com.renato.projects.ecommerce.controller.builder.*;
import com.renato.projects.ecommerce.domain.*;
import com.renato.projects.ecommerce.domain.enums.RoleName;
import com.renato.projects.ecommerce.domain.enums.Status;
import com.renato.projects.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class PedidoControllerTeste {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PedidoRepository pedidoRepository;
    private UserDetailsImpl user;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EnderecoRepository enderecoRepository;
    @Autowired
    private ProdutoPedidoRepository produtoPedidoRepository;
    private Pedido pedido;
    private Produto produto;
    @BeforeEach
    @Transactional
    void autenticar() {

        Categoria categoria = new CategoriaBuilder()
                        .comNome("Videogames")
                        .build();

        produto = new ProdutoBuilder()
                        .comNome("Switch")
                        .comPreco(BigDecimal.valueOf(3500))
                        .comCategoria(categoria)
                        .build();

        user = new UserBuilder().build();
        Role role = roleRepository.findByName(RoleName.ROLE_CUSTOMER)
                .orElseThrow();
        user.setRoles(Set.of(role));

        Endereco endereco = new EnderecoBuilder().build();

        Cliente cliente = new ClienteBuilder()
                        .comNome("Renato")
                        .comUser(user)
                        .build();
        user.setCliente(cliente);
        cliente.setEndereco(List.of(endereco));
        endereco.setCliente(cliente);

        pedido = new PedidoBuilder()
                        .comCliente(cliente)
                        .build();
        cliente.setPedidos(List.of(pedido));

        userRepository.save(user);
        categoriaRepository.save(categoria);
        produtoRepository.save(produto);
        clienteRepository.save(cliente);
        enderecoRepository.save(endereco);
        pedidoRepository.save(pedido);

    }

    @Test
    public void deveAdicionarNovoItemAoCarrinhoQuandoOItemNaoExistirNoCarrinho() throws Exception {

        String json = """
            {
                "idProduto": 1,
                "qtd": 1
            }
            """;

        mockMvc.perform(
                        patch("/pedidos/cart")
                                .with(user(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        Pedido pedido = pedidoRepository
                .findByClienteAndStatus(user.getCliente(), Status.INICIADO)
                .orElseThrow();

        ProdutoPedido item = produtoPedidoRepository
                .findByProdutoIdAndPedidoId(1L, pedido.getId())
                .orElseThrow();

        // ✔ existe exatamente esse item
        assertNotNull(item.getId());

        // ✔ produto correto
        assertEquals(1L, item.getProduto().getId());

        // ✔ quantidade correta
        assertEquals(1L, item.getQuantidade());

        // ✔ valor unitário correto
        assertEquals(0, item.getValorUnitario().compareTo(BigDecimal.valueOf(3500)));

        // ✔ associação correta
        assertEquals(pedido.getId(), item.getPedido().getId());
    }

    @Test
    public void deveAdicionarNovoItemAoCarrinhoQuandoOItemExistirNoCarrinho() throws Exception {
        ProdutoPedido pp = new ProdutoPedido();
        pp.setPedido(pedido);
        pp.setProduto(produto);
        pp.setQuantidade(1L);
        pedido.setProdutosPedidos(List.of(pp));
        produtoPedidoRepository.save(pp);

        String json = """
            {
                "idProduto": 1,
                "qtd": 1
            }
            """;

        mockMvc.perform(
                        patch("/pedidos/cart")
                                .with(user(user))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk());

        ProdutoPedido item = produtoPedidoRepository
                .findByProdutoIdAndPedidoId(1L, pedido.getId())
                .orElseThrow();

        // ✔ quantidade aumentada para 2
        assertEquals(2L, item.getQuantidade());

        // ✔ valor unitário mantém correto
        assertEquals(0, item.getValorUnitario().compareTo(BigDecimal.valueOf(3500)));

        // ✔ associação correta
        assertEquals(pedido.getId(), item.getPedido().getId());
    }

    @Test
    public void naoDeveAdicionarNovoItemQuandoNaoEstiverMaisDisponivelNoEstoque(){

    }

}
