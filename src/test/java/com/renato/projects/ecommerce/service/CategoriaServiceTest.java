package com.renato.projects.ecommerce.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import com.renato.projects.ecommerce.controller.dto.categoria.PostCategoriaDTO;
import com.renato.projects.ecommerce.controller.dto.categoria.ReadCategoriaDTO;
import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.repository.CategoriaRepository;

public class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    public CategoriaServiceTest() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testPostCategoria() {
        // Arrange
        PostCategoriaDTO dto = new PostCategoriaDTO("Eletrônicos", "Produtos eletrônicos");

        // Mock
        when(categoriaRepository.save(any(Categoria.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act: 
        ReadCategoriaDTO result = categoriaService.postCategoria(dto);

        // Assert: 
        assertNotNull(result);
        assertEquals("Eletrônicos", result.nome());
        assertEquals("Produtos eletrônicos", result.descricao());
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }
    
    @Test
    void testPostCategoriaErroUnicidadeNome() {
        // Arrange
        PostCategoriaDTO dto = new PostCategoriaDTO("Eletrônicos", "Produtos eletrônicos");

        // Mock
        when(categoriaRepository.save(any(Categoria.class)))
                .thenThrow(new DataIntegrityViolationException("Unique constraint violation"));

        // Act & Assert:
        DataIntegrityViolationException exception = 
            assertThrows(DataIntegrityViolationException.class, () -> {
                categoriaService.postCategoria(dto);
            });
        assertTrue(exception.getMessage().contains("Unique constraint violation"));
        verify(categoriaRepository, times(1)).save(any(Categoria.class));
    }
    
    @Test
    void testPostCategoriaBlankFields() {
        // Arrange
        PostCategoriaDTO dto = new PostCategoriaDTO("   ", "     ");

        // Mock
        when(categoriaRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Column 'nome' cannot be null or blank"));
        System.out.println("dentro do teste");
        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            categoriaService.postCategoria(dto);
        });

        assertTrue(exception.getMessage().contains("nome"));
        verify(categoriaRepository, times(1)).save(any());
    }

    @Test
    void testPostCategoriaNullFields() {
        // Arrange: DTO com nome nulo
        PostCategoriaDTO dto = new PostCategoriaDTO(null, null);

        // Simula o banco lançando DataIntegrityViolationException
        when(categoriaRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Column 'nome' cannot be null"));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            categoriaService.postCategoria(dto);
        });

        assertTrue(exception.getMessage().contains("nome"));
        verify(categoriaRepository, times(1)).save(any());
    }

    @Test
    void testPostCategoriaEmptyFields() {
        // Arrange: DTO com string vazia
        PostCategoriaDTO dto = new PostCategoriaDTO("", "");

        // Simula o banco lançando DataIntegrityViolationException
        when(categoriaRepository.save(any()))
                .thenThrow(new DataIntegrityViolationException("Column 'nome' cannot be empty"));

        // Act & Assert
        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            categoriaService.postCategoria(dto);
        });

        assertTrue(exception.getMessage().contains("nome"));
        verify(categoriaRepository, times(1)).save(any());
    }
}