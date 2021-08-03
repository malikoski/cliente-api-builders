CREATE TABLE cliente(
	id SERIAL PRIMARY KEY,
	uuid uuid UNIQUE DEFAULT gen_random_uuid(),
	nome VARCHAR(255) NOT NULL,
	cpf VARCHAR(11) NOT NULL,
	idade SMALLINT NOT NULL,
	endereco VARCHAR(255),
	endereco_numero VARCHAR(10),
	cep VARCHAR(8),
	cidade VARCHAR(100),
	uf VARCHAR(2),
	email VARCHAR(150) NOT NULL,
	telefone VARCHAR(25) NOT NULL
)