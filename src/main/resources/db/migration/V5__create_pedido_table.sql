CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,

    valor_total NUMERIC(10,2) NOT NULL,

    data_iniciacao DATE,
    data_criacao DATE NOT NULL,

    status VARCHAR(50) NOT NULL,

    cliente_id BIGINT NOT NULL,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (id)
);
