CREATE DATABASE  feature_store;
CREATE SCHEMA feature_store;

CREATE SEQUENCE feature_store.dataset_num_seq
START 1
INCREMENT 1;

CREATE SEQUENCE feature_store.feature_num_seq
START 1
INCREMENT 1;

CREATE SEQUENCE feature_store.versao_num_seq
START 1
INCREMENT 1;

CREATE SEQUENCE feature_store.log_acesso_num_seq
START 1
INCREMENT 1;

CREATE TABLE feature_store.usuario (
	cpf CHAR(11) NOT NULL,
	nome VARCHAR(100) NOT NULL,
	email VARCHAR(255) NOT NULL,
	senha VARCHAR(255) NOT NULL,
	data_criacao DATE NOT NULL,
	CONSTRAINT pk_usuario PRIMARY KEY (cpf)
);

CREATE TABLE feature_store.dataset (
	id INT NOT NULL DEFAULT NEXTVAL('feature_store.dataset_num_seq'),
	nome VARCHAR(100) NOT NULL,
	descricao VARCHAR(255) NOT NULL,
	data_criacao DATE NOT NULL,
	cpf_usuario CHAR(11) NOT NULL,
	CONSTRAINT pk_dataset PRIMARY KEY (id),
	CONSTRAINT fk_dataset_usuario FOREIGN KEY (cpf_usuario) REFERENCES feature_store.usuario(cpf)
);

CREATE TABLE feature_store.versao (
	id INT NOT NULL DEFAULT NEXTVAL('feature_store.versao_num_seq'),
	arquivo_path VARCHAR(255) NOT NULL,
	numero_versao VARCHAR(20) NOT NULL,
	data_inclusao DATE NOT NULL,
	id_dataset INT NOT NULL,
	id_versao_base INT,
	CONSTRAINT pk_versao PRIMARY KEY (id),
	CONSTRAINT fk_versao_dataset FOREIGN KEY (id_dataset) REFERENCES feature_store.dataset(id)
);

ALTER TABLE feature_store.versao ADD CONSTRAINT fk_derivado_baseado FOREIGN KEY (id_versao_base) REFERENCES feature_store.versao(id);

CREATE TABLE feature_store.feature (
	id INT NOT NULL DEFAULT NEXTVAL('feature_store.feature_num_seq'),
	nome VARCHAR(100) NOT NULL,
	tipo_dado VARCHAR(50) NOT NULL,
	descricao VARCHAR(255) NOT NULL,
	id_versao INT NOT NULL,
	CONSTRAINT pk_feature PRIMARY KEY (id),
	CONSTRAINT fk_feature_versao FOREIGN KEY (id_versao) REFERENCES feature_store.versao(id)
);

CREATE TABLE feature_store.log_acesso (
	id INT NOT NULL DEFAULT NEXTVAL('feature_store.log_acesso_num_seq'),
	tipo_operacao INT NOT NULL,
	data_hora TIMESTAMP NOT NULL,
	cpf_usuario CHAR(11) NOT NULL,
	id_dataset INT NOT NULL,
	CONSTRAINT pk_log_acesso PRIMARY KEY (id),
	CONSTRAINT fk_log_acesso_usuario FOREIGN KEY (cpf_usuario) REFERENCES feature_store.usuario(cpf),
	CONSTRAINT fk_log_acesso_dataset FOREIGN KEY (id_dataset) REFERENCES feature_store.dataset(id),
	CONSTRAINT ck_tipo_operacao CHECK (tipo_operacao = 0 OR tipo_operacao = 1)
);
