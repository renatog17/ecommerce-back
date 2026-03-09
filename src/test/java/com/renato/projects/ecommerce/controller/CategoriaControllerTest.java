package com.renato.projects.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.renato.projects.ecommerce.controller.dto.categoria.PostCategoriaDTO;
import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        categoriaRepository.deleteAll(); // garante ambiente limpo
    }

    @Test
    void testPostCategoriaSuccess() throws Exception {
        PostCategoriaDTO dto = new PostCategoriaDTO("Eletrônicos", "Produtos eletrônicos");

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"))
            .andExpect(jsonPath("$.nome").value("Eletrônicos"))
            .andExpect(jsonPath("$.descricao").value("Produtos eletrônicos"))
            .andExpect(jsonPath("$.ativo").value(true));

        // Verifica se o banco foi realmente populado
        assert(categoriaRepository.count() == 1);
    }

    @Test
    void testPostCategoriaNomeNull() throws Exception {
        PostCategoriaDTO dto = new PostCategoriaDTO(null, "Descrição válida");

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testPostCategoriaBlankFields() throws Exception {
        PostCategoriaDTO dto = new PostCategoriaDTO("   ", "Descrição válida");

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testPostCategoriaEmptyFields() throws Exception {
        PostCategoriaDTO dto = new PostCategoriaDTO("", "Descrição válida");

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());
    }

    @Test
    void testPostCategoriaDuplicateName() throws Exception {
        // Cria categoria no banco
        Categoria categoria = new Categoria();
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Produtos eletrônicos");
        categoria.setAtivo(true);
        categoriaRepository.save(categoria);

        // Tenta criar novamente com mesmo nome
        PostCategoriaDTO dto = new PostCategoriaDTO("Eletrônicos", "Produtos eletrônicos");

        mockMvc.perform(post("/categorias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isConflict()); // depende de como você trata DataIntegrityViolationException
    }
}