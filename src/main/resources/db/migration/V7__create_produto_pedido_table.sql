CREATE TABLE produto_pedido (
    id BIGSERIAL PRIMARY KEY,

    valor_unitario NUMERIC(10,2) NOT NULL,
    quantidade BIGINT NOT NULL,

    produto_id BIGINT NOT NULL,
    pedido_id BIGINT NOT NULL,

    CONSTRAINT fk_produto_pedido_produto
        FOREIGN KEY (produto_id)
        REFERENCES produto (id),

    CONSTRAINT fk_produto_pedido_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido (id)
);
