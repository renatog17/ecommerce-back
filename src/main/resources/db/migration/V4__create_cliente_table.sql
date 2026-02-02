CREATE TABLE cliente (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    telefone VARCHAR(50),
    data_cadastro DATE NOT NULL,
    cpf VARCHAR(14) NOT NULL,
    user_id BIGINT,
    CONSTRAINT uk_cliente_cpf UNIQUE (cpf),
    CONSTRAINT uk_cliente_email UNIQUE (email),
    CONSTRAINT fk_cliente_user
        FOREIGN KEY (user_id)
        REFERENCES "users" (id)
);
