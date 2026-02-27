CREATE TABLE pedido (
    id BIGSERIAL PRIMARY KEY,

    valor_total NUMERIC(10,2) NOT NULL,

    data_iniciacao DATE NOT NULL,
    data_criacao DATE ,

    status VARCHAR(50) NOT NULL,

    cliente_id BIGINT NOT NULL,

    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente (id)
);
