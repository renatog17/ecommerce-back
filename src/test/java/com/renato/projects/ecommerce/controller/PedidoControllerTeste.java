package com.renato.projects.ecommerce.controller;

import com.renato.projects.ecommerce.controller.builder.*;
import com.renato.projects.ecommerce.domain.*;
import com.renato.projects.ecommerce.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Test
    public void deveAdicionarNovoItemAoCarrinhoQuandoOItemNaoExistirNoCarrinho(){
        Categoria categoria = categoriaRepository.save(
                new CategoriaBuilder()
                        .comNome("Videogames")
                        .build()
        );

        Produto produto = produtoRepository.save(
                new ProdutoBuilder()
                        .comNome("Switch")
                        .comPreco(BigDecimal.valueOf(3500))
                        .comCategoria(categoria)
                        .build()
        );

        UserDetailsImpl user = userRepository.save(
                new UserBuilder().build()
        );

        Cliente cliente = clienteRepository.save(
                new ClienteBuilder()
                        .comNome("Renato")
                        .comUser(user)
                        .build()
        );

        Pedido pedido = pedidoRepository.save(
                new PedidoBuilder()
                        .comCliente(cliente)
                        .build()
        );

        // aqui você chama o método que está testando
        mockMvc.perform();


    }

    @Test
    public void deveAdicionarNovoItemAoCarrinhoQuandoOItemExistirNoCarrinho(){

    }

    @Test
    public void naoDeveAdicionarNovoItemQuandoNaoEstiverMaisDisponivelNoEstoque(){

    }

}
