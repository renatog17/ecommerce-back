package com.renato.projects.ecommerce.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.repository.CategoriaRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class CategoriaControllerTeste {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CategoriaRepository categoriaRepository;
    
	@Test
    void deveCriarCategoria() throws Exception {

        String json = """
            {
                "nome": "Eletrônicos",
                "descricao": "Produtos eletrônicos"
            }
            """;

        mockMvc.perform(
                post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "http://localhost/categorias/1"))
                .andExpect(jsonPath("$.descricao").value("Produtos eletrônicos"))
                .andExpect(jsonPath("$.nome").value("Eletrônicos"));
    }
    
    @Test
    void naoDeveCriarCategoriaComNomeEmBranco() throws Exception {

        String json = """
            {
                "nome": "",
                "descricao": "teste"
            }
            """;

        mockMvc.perform(
                post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("nome"))
                .andExpect(jsonPath("$[0].status").value(400))
                .andExpect(jsonPath("$[0].path").value("/categorias"))
        		.andExpect(jsonPath("$[0].message")
        				.value("Nome da categoria não pode estar em branco"));
    }
 
    @Test
    void deveListarApenasCategoriasAtivas() throws Exception {

        // Arrange
        Categoria c1 = new Categoria();
        c1.setNome("Livros");
        c1.setDescricao("Livros para todas as idades");
        c1.setAtivo(true);
        categoriaRepository.save(c1);

        Categoria c2 = new Categoria();
        c2.setNome("Eletrônicos");
        c2.setDescricao("Produtos tech");
        c2.setAtivo(true);
        categoriaRepository.save(c2);

        Categoria c3 = new Categoria();
        c3.setNome("Arquivadas");
        c3.setDescricao("Categoria inativa");
        c3.setAtivo(false);
        categoriaRepository.save(c3);

        // Act + Assert
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].nome").value("Livros"))
                .andExpect(jsonPath("$[1].nome").value("Eletrônicos"));
    }
    
    @Test
    void deveRetornarListaVaziaQuandoNaoExistiremCategoriasAtivas() throws Exception {

        // Arrange
        Categoria c1 = new Categoria();
        c1.setNome("Arquivadas");
        c1.setDescricao("Categoria inativa");
        c1.setAtivo(false);
        categoriaRepository.save(c1);

        // Act + Assert
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }
    
    @Test
    void deveListarUmaCategoriaAtiva() throws Exception {

        // Arrange
        Categoria c1 = new Categoria();
        c1.setNome("Livros");
        c1.setDescricao("Livros para todas as idades");
        c1.setAtivo(true);
        categoriaRepository.save(c1);

        // Act + Assert
        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].nome").value("Livros"));
    }
    
    @Test
    void deveBuscarCategoriaPorId() throws Exception {

        Categoria categoria = new Categoria();
        categoria.setNome("Livros");
        categoria.setDescricao("Categoria livros");
        categoria.setAtivo(true);

        Categoria salva = categoriaRepository.save(categoria);

        mockMvc.perform(get("/categorias/{id}", salva.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salva.getId()))
                .andExpect(jsonPath("$.nome").value("Livros"))
                .andExpect(jsonPath("$.descricao").value("Categoria livros"));
    }
    
    @Test
    void naoDeveBuscarCategoriaInativaPorId() throws Exception {

        Categoria categoria = new Categoria();
        categoria.setNome("Livros");
        categoria.setDescricao("Categoria livros");
        categoria.setAtivo(false);

        Categoria salva = categoriaRepository.save(categoria);

        mockMvc.perform(get("/categorias/{id}", salva.getId()))
                .andExpect(status().isNotFound());
    }
    
   
    @Test
    void deveAtualizarDescricao() throws Exception {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNome("Games");
        categoria.setDescricao("Categoria antiga");
        categoria.setAtivo(true);

        Categoria salva = categoriaRepository.save(categoria);

        String patchJson = """
            {
                "descricao": "Nova descrição"
            }
            """;
        mockMvc.perform(
                patch("/categorias/{id}", salva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salva.getId()))
                .andExpect(jsonPath("$.nome").value("Games"))
                .andExpect(jsonPath("$.descricao").value("Nova descrição"));
    }
    
    @Test
    void naoDeveAtualizarDescricaoCampoInvalido() throws Exception {

        Categoria categoria = new Categoria();
        categoria.setNome("Games");
        categoria.setDescricao("Categoria antiga");
        categoria.setAtivo(true);

        Categoria salva = categoriaRepository.save(categoria);

        String patchJson = """
            {
                "descricao": ""
            }
            """;
        mockMvc.perform(
                patch("/categorias/{id}", salva.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(patchJson)
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].field").value("descricao"))
                .andExpect(jsonPath("$[0].message").value("Descrição não pode estar em branco"));
    }
    
    
    @Test
    void deveDeletarCategoria() throws Exception {

        // Arrange
        Categoria categoria = new Categoria();
        categoria.setNome("Roupas");
        categoria.setDescricao("Categoria roupas");
        categoria.setAtivo(true);

        Categoria salva = categoriaRepository.save(categoria);

        // Act
        mockMvc.perform(delete("/categorias/{id}", salva.getId()))
                .andExpect(status().isNoContent());

        // Assert
        assertFalse(categoriaRepository.findById(salva.getId()).isPresent());
    }
}