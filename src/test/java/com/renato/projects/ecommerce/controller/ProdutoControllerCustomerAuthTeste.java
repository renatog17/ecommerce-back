package com.renato.projects.ecommerce.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import com.renato.projects.ecommerce.domain.Role;
import com.renato.projects.ecommerce.domain.enums.RoleName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.renato.projects.ecommerce.domain.Categoria;
import com.renato.projects.ecommerce.domain.Produto;
import com.renato.projects.ecommerce.domain.UserDetailsImpl;
import com.renato.projects.ecommerce.repository.CategoriaRepository;
import com.renato.projects.ecommerce.repository.ProdutoRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Sql(value = "/cleanup.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProdutoControllerCustomerAuthTeste {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProdutoRepository produtoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@BeforeEach
	void autenticar() {

		UserDetailsImpl user = new UserDetailsImpl(
				"renato@email.com",
				"123"
		);

		Role role = new Role();
		role.setName(RoleName.ROLE_CUSTOMER);

		Set<Role> roles = new HashSet<Role>();
		roles.add(role);
		user.setRoles(roles);

		Authentication authentication =
				new UsernamePasswordAuthenticationToken(
						user,
						null,
						user.getAuthorities()
				);

		SecurityContextHolder.getContext()
				.setAuthentication(authentication);
	}
	
	// ===============================
	// POST
	// ===============================

	@Test
	void naoDevePermitirCustomerDeCriarProdutoHappyPath() throws Exception {

		Categoria categoria = new Categoria();
		categoria.setNome("Eletrônicos");
		categoria.setDescricao("Categoria tech");
		categoria.setAtivo(true);
		categoriaRepository.save(categoria);

		String json = """
				{
				    "nome": "Notebook",
				    "descricao": "Notebook Lenovo",
				    "quantidade": "10",
				    "preco": 3500,
				    "idCategoria": 1
				}
				""";

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isForbidden());
	}

	@Test
	void naoDeveCriarProdutoSemNome() throws Exception {

		Categoria categoria = new Categoria();
		categoria.setNome("Eletrônicos");
		categoria.setDescricao("Categoria tech");
		categoria.setAtivo(true);
		categoriaRepository.save(categoria);

		String json = """
				{
				    "nome": "",
				    "descricao": "Notebook Lenovo",
				    "quantidade": "10",
				    "preco": 3500,
				    "idCategoria": 1
				}
				""";

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0].field").value("nome"))
				.andExpect(jsonPath("$[0].status").value(400)).andExpect(jsonPath("$[0].path").value("/produtos"))
				.andExpect(jsonPath("$[0].message").value("Nome do produto não pode estar em branco"));
	}

	@Test
	void naoDeveCriarProdutoSemQuantidade() throws Exception {

		Categoria categoria = new Categoria();
		categoria.setNome("Eletrônicos");
		categoria.setDescricao("Categoria tech");
		categoria.setAtivo(true);
		categoriaRepository.save(categoria);

		String json = """
				{
				    "nome": "Notebook",
				    "descricao": "Notebook Lenovo",
				    "preco": 3500,
				    "idCategoria": 1
				}
				""";

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0].field").value("quantidade"))
				.andExpect(jsonPath("$[0].status").value(400)).andExpect(jsonPath("$[0].path").value("/produtos"))
				.andExpect(jsonPath("$[0].message").value("Quantidade não pode estar em branco"));
	}

	@Test
	void naoDeveCriarProdutoSemPreco() throws Exception {

		Categoria categoria = new Categoria();
		categoria.setNome("Eletrônicos");
		categoria.setDescricao("Categoria tech");
		categoria.setAtivo(true);
		categoriaRepository.save(categoria);

		String json = """
				{
				    "nome": "Produto teste",
				    "descricao": "Notebook Lenovo",
				    "quantidade": "10",
				    "idCategoria": 1
				}
				""";

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$[0].field").value("preco"))
				.andExpect(jsonPath("$[0].status").value(400))
				.andExpect(jsonPath("$[0].path").value("/produtos"))
				.andExpect(jsonPath("$[0].message").value("Preço do produto não pode estar em branco"));
	}

	@Test
	void naoDeveCriarProdutoSemCategoria() throws Exception {

		Categoria categoria = new Categoria();
		categoria.setNome("Eletrônicos");
		categoria.setDescricao("Categoria tech");
		categoria.setAtivo(true);
		categoriaRepository.save(categoria);

		String json = """
				{
				    "nome": "Teste produto",
				    "descricao": "Notebook Lenovo",
				    "quantidade": "10",
				    "preco": 3500
				}
				""";

		mockMvc.perform(post("/produtos").contentType(MediaType.APPLICATION_JSON).content(json))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0].field").value("idCategoria"))
				.andExpect(jsonPath("$[0].status").value(400))
				.andExpect(jsonPath("$[0].path").value("/produtos"))
				.andExpect(jsonPath("$[0].message").value("Id da categoria não pode estar em branco"));
	}

	// ===============================
	// GET
	// ===============================

	@Test
	void deveObterProdutoAtivoTrue() throws Exception {

		Categoria c1 = new Categoria();
		c1.setAtivo(true);
		c1.setDescricao("Descrição teste");
		c1.setNome("Nome teste");
		categoriaRepository.save(c1);
		
		Produto produto = new Produto();
		produto.setNome("Livro");
		produto.setPreco(new BigDecimal("50"));
		produto.setAtivo(true);
		produto.setQuantidade(15L);
		produto.setCategoria(c1);
		
		Produto salvo = produtoRepository.save(produto);

		mockMvc.perform(get("/produtos/{id}", salvo.getId())).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(salvo.getId())).andExpect(jsonPath("$.nome").value("Livro"))
				.andExpect(jsonPath("$.preco").value(50));
	}

	@Test
	void naoDeveObterProdutoAtivoFalse() throws Exception {

		Categoria c1 = new Categoria();
		c1.setAtivo(true);
		c1.setDescricao("Descrição teste");
		c1.setNome("Nome teste");
		categoriaRepository.save(c1);
		
		Produto produto = new Produto();
		produto.setNome("Mouse");
		produto.setPreco(new BigDecimal("100"));
		produto.setAtivo(false);
		produto.setQuantidade(15l);
		produto.setCategoria(c1);

		Produto salvo = produtoRepository.save(produto);

		mockMvc.perform(get("/produtos/{id}", salvo.getId())).andExpect(status().isNotFound());
	}

	// ===============================
	// GET ALL
	// ===============================

	@Test
	void deveListarUmProduto() throws Exception {

	    // Arrange
	    Categoria c1 = new Categoria();
	    c1.setAtivo(true);
	    c1.setDescricao("Descrição teste");
	    c1.setNome("Nome teste");
	    categoriaRepository.save(c1);

	    Produto p = new Produto();
	    p.setNome("Livro");
	    p.setPreco(new BigDecimal("50"));
	    p.setAtivo(true);
	    p.setQuantidade(15L);
	    p.setCategoria(c1);
	    produtoRepository.save(p);

	    // Act + Assert
	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20")
	            .contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isOk())

	            // paginação
	            .andExpect(jsonPath("$.content.length()").value(1))
	            .andExpect(jsonPath("$.totalElements").value(1))
	            .andExpect(jsonPath("$.totalPages").value(1))
	            .andExpect(jsonPath("$.number").value(0))

	            // dados do produto
	            .andExpect(jsonPath("$.content[0].nome").value("Livro"))
	            .andExpect(jsonPath("$.content[0].preco").value(50))
	            .andExpect(jsonPath("$.content[0].quantidade").value(15));
	}

	@Test
	void deveListarVariosProdutos() throws Exception {

	    // Arrange
	    Categoria c1 = new Categoria();
	    c1.setAtivo(true);
	    c1.setDescricao("Descrição teste");
	    c1.setNome("Nome teste");
	    categoriaRepository.save(c1);

	    Produto p1 = new Produto();
	    p1.setNome("Livro");
	    p1.setPreco(new BigDecimal("50"));
	    p1.setAtivo(true);
	    p1.setQuantidade(15L);
	    p1.setCategoria(c1);

	    Produto p2 = new Produto();
	    p2.setNome("Mouse");
	    p2.setPreco(new BigDecimal("100"));
	    p2.setAtivo(true);
	    p2.setQuantidade(15L);
	    p2.setCategoria(c1);

	    produtoRepository.save(p1);
	    produtoRepository.save(p2);

	    // Act + Assert
	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // estrutura da paginação
	        .andExpect(jsonPath("$.content.length()").value(2))
	        .andExpect(jsonPath("$.totalElements").value(2))
	        .andExpect(jsonPath("$.totalPages").value(1))
	        .andExpect(jsonPath("$.number").value(0))

	        // valida dados
	        .andExpect(jsonPath("$.content[0].nome").value("Livro"))
	        .andExpect(jsonPath("$.content[1].nome").value("Mouse"));
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoHouverProdutos() throws Exception {

	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // página vazia
	        .andExpect(jsonPath("$.content.length()").value(0))

	        // metadados da paginação
	        .andExpect(jsonPath("$.totalElements").value(0))
	        .andExpect(jsonPath("$.totalPages").value(0))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void naoDeveListarUmProdutoComAtivoFalse() throws Exception {

	    // Arrange
	    Categoria c1 = new Categoria();
	    c1.setAtivo(true);
	    c1.setDescricao("Descrição teste");
	    c1.setNome("Nome teste");
	    categoriaRepository.save(c1);

	    Produto p = new Produto();
	    p.setNome("Livro");
	    p.setPreco(new BigDecimal("50"));
	    p.setAtivo(false);
	    p.setQuantidade(15L);
	    p.setCategoria(c1);
	    produtoRepository.save(p);

	    // Act + Assert
	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // lista vazia porque ativo = false
	        .andExpect(jsonPath("$.content.length()").value(0))

	        // metadados da paginação
	        .andExpect(jsonPath("$.totalElements").value(0))
	        .andExpect(jsonPath("$.totalPages").value(0))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void deveListarApenasProdutosAtivosDentreAtivoFalseETrue() throws Exception {

	    // Arrange
	    Categoria c1 = new Categoria();
	    c1.setAtivo(true);
	    c1.setDescricao("Descrição teste");
	    c1.setNome("Nome teste");
	    categoriaRepository.save(c1);

	    Produto p1 = new Produto();
	    p1.setNome("Livro");
	    p1.setPreco(new BigDecimal("50"));
	    p1.setAtivo(true);
	    p1.setQuantidade(15L);
	    p1.setCategoria(c1);

	    Produto p2 = new Produto();
	    p2.setNome("Mouse");
	    p2.setPreco(new BigDecimal("100"));
	    p2.setAtivo(true);
	    p2.setQuantidade(15L);
	    p2.setCategoria(c1);

	    Produto p3 = new Produto();
	    p3.setNome("Teclado");
	    p3.setPreco(new BigDecimal("200"));
	    p3.setAtivo(false);
	    p3.setQuantidade(15L);
	    p3.setCategoria(c1);

	    produtoRepository.save(p1);
	    produtoRepository.save(p2);
	    produtoRepository.save(p3);

	    // Act + Assert
	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // apenas produtos ativos
	        .andExpect(jsonPath("$.content.length()").value(2))
	        .andExpect(jsonPath("$.content[0].nome").exists())
	        .andExpect(jsonPath("$.content[1].nome").exists())

	        // metadados da página
	        .andExpect(jsonPath("$.totalElements").value(2))
	        .andExpect(jsonPath("$.totalPages").value(1))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void naoDeveListarVariosProdutosComAtivoFalse() throws Exception {

	    // Arrange
	    Categoria c1 = new Categoria();
	    c1.setAtivo(true);
	    c1.setDescricao("Descrição teste");
	    c1.setNome("Nome teste");
	    categoriaRepository.save(c1);

	    Produto p1 = new Produto();
	    p1.setNome("Livro");
	    p1.setPreco(new BigDecimal("50"));
	    p1.setAtivo(false);
	    p1.setQuantidade(15L);
	    p1.setCategoria(c1);

	    Produto p2 = new Produto();
	    p2.setNome("Mouse");
	    p2.setPreco(new BigDecimal("100"));
	    p2.setAtivo(false);
	    p2.setQuantidade(15L);
	    p2.setCategoria(c1);

	    produtoRepository.save(p1);
	    produtoRepository.save(p2);

	    // Act + Assert
	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // nenhum produto ativo
	        .andExpect(jsonPath("$.content.length()").value(0))

	        // metadados da página
	        .andExpect(jsonPath("$.totalElements").value(0))
	        .andExpect(jsonPath("$.totalPages").value(0))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void deveRetornarListaVaziaQuandoNaoHouverProdutosCadastrados() throws Exception {

	    mockMvc.perform(get("/produtos")
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())

	        // lista vazia
	        .andExpect(jsonPath("$.content.length()").value(0))

	        // metadados da página
	        .andExpect(jsonPath("$.totalElements").value(0))
	        .andExpect(jsonPath("$.totalPages").value(0))
	        .andExpect(jsonPath("$.number").value(0));
	}
	// ===============================
	// FILTRO POR CATEGORIA
	// ===============================

	@Test
	void deveRetornarVazioQuandoCategoriaNaoPossuirProdutos() throws Exception {

	    Categoria categoria = new Categoria();
	    categoria.setNome("Livros");
	    categoria.setDescricao("Categoria livros");
	    categoria.setAtivo(true);
	    categoriaRepository.save(categoria);

	    mockMvc.perform(get("/produtos")
	            .param("categoriaId", categoria.getId().toString())
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content.length()").value(0))
	        .andExpect(jsonPath("$.totalElements").value(0))
	        .andExpect(jsonPath("$.totalPages").value(0))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void deveListarProdutosDeUmaCategoria() throws Exception {

	    Categoria categoria = new Categoria();
	    categoria.setNome("Games");
	    categoria.setDescricao("Jogos");
	    categoria.setAtivo(true);
	    categoriaRepository.save(categoria);

	    Produto p = new Produto();
	    p.setNome("PS5");
	    p.setPreco(new BigDecimal("5000"));
	    p.setAtivo(true);
	    p.setCategoria(categoria);
	    p.setQuantidade(15L);
	    produtoRepository.save(p);

	    mockMvc.perform(get("/produtos")
	            .param("categoriaId", categoria.getId().toString())
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content.length()").value(1))
	        .andExpect(jsonPath("$.content[0].nome").value("PS5"))
	        .andExpect(jsonPath("$.totalElements").value(1))
	        .andExpect(jsonPath("$.totalPages").value(1))
	        .andExpect(jsonPath("$.number").value(0));
	}

	@Test
	void deveListarProdutosQuandoExistiremVariasCategorias() throws Exception {

	    Categoria c1 = new Categoria();
	    c1.setNome("Games");
	    c1.setDescricao("Jogos");
	    c1.setAtivo(true);

	    Categoria c2 = new Categoria();
	    c2.setNome("Livros");
	    c2.setDescricao("Livros");
	    c2.setAtivo(true);

	    categoriaRepository.save(c1);
	    categoriaRepository.save(c2);

	    Produto p1 = new Produto();
	    p1.setNome("PS5");
	    p1.setPreco(new BigDecimal("5000"));
	    p1.setCategoria(c1);
	    p1.setAtivo(true);
	    p1.setQuantidade(15L);

	    Produto p2 = new Produto();
	    p2.setNome("Clean Code");
	    p2.setPreco(new BigDecimal("100"));
	    p2.setCategoria(c2);
	    p2.setAtivo(true);
	    p2.setQuantidade(15L);

	    produtoRepository.save(p1);
	    produtoRepository.save(p2);

	    mockMvc.perform(get("/produtos")
	            .param("categoriaId", c1.getId().toString())
	            .param("page", "0")
	            .param("size", "20"))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content.length()").value(1))
	        .andExpect(jsonPath("$.content[0].nome").value("PS5"))
	        .andExpect(jsonPath("$.totalElements").value(1))
	        .andExpect(jsonPath("$.totalPages").value(1))
	        .andExpect(jsonPath("$.number").value(0));
	}

	// ===============================
	// PATCH PREÇO
	// ===============================

	@Test
	void naoDevePermitirCustomerDeEditarPrecoHappyPath() throws Exception {

		Categoria c1 = new Categoria();
		c1.setAtivo(true);
		c1.setDescricao("Descrição teste");
		c1.setNome("Nome teste");
		categoriaRepository.save(c1);
		
		Produto produto = new Produto();
		produto.setNome("Notebook");
		produto.setPreco(new BigDecimal("3000"));
		produto.setAtivo(true);
		produto.setQuantidade(15L);
		produto.setCategoria(c1);
		
		Produto salvo = produtoRepository.save(produto);

		String json = """
				{
				    "novoPreco": 3500.00
				}
				""";

		mockMvc.perform(patch("/produtos/{id}/alterar-preco", salvo.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isForbidden());
	}

	@Test
	void naoDeveEditarPrecoQuandoPrecoNulo() throws Exception {

		Categoria c1 = new Categoria();
		c1.setAtivo(true);
		c1.setDescricao("Descrição teste");
		c1.setNome("Nome teste");
		categoriaRepository.save(c1);
		
		Produto produto = new Produto();
		produto.setNome("Notebook");
		produto.setPreco(new BigDecimal("3000"));
		produto.setAtivo(true);
		produto.setQuantidade(15L);
		produto.setCategoria(c1);

		Produto salvo = produtoRepository.save(produto);

		String json = """
				{
				    "novoPreco": null
				}
				""";

		mockMvc.perform(patch("/produtos/{id}/alterar-preco", salvo.getId()).contentType(MediaType.APPLICATION_JSON)
				.content(json)).andExpect(status().isBadRequest());
	}

	// ===============================
	// DELETE
	// ===============================

	@Test
	void deveDesativarProduto() throws Exception {

		Categoria c1 = new Categoria();
		c1.setAtivo(true);
		c1.setDescricao("Descrição teste");
		c1.setNome("Nome teste");
		categoriaRepository.save(c1);
		
		Produto produto = new Produto();
		produto.setNome("Mouse");
		produto.setPreco(new BigDecimal("100"));
		produto.setAtivo(true);
		produto.setCategoria(c1);
		produto.setQuantidade(18l);
		
		Produto salvo = produtoRepository.save(produto);

		mockMvc.perform(delete("/produtos/{id}", salvo.getId())).andExpect(status().isNoContent());
	}

}