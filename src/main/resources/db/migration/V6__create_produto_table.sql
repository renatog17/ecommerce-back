CREATE TABLE produto (
    id BIGSERIAL PRIMARY KEY,

    nome VARCHAR(255) NOT NULL,
    descricao TEXT,

    estoque BIGINT NOT NULL,

    preco NUMERIC(10,2) NOT NULL,

    categoria_id BIGINT NOT NULL,

    CONSTRAINT fk_produto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria (id)
);
