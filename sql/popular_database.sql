INSERT INTO feature_store.usuario (cpf, nome, email, senha, data_criacao) VALUES ('12345678910', 'Gabriel', 'gabriel@email', '123', CURRENT_DATE);
INSERT INTO feature_store.usuario (cpf, nome, email, senha, data_criacao) VALUES ('98765432101', 'Bianca', 'bianca@email', '4567', CURRENT_DATE);

INSERT INTO feature_store.dataset (nome, descricao, data_criacao, cpf_usuario) VALUES ('R.U', 'Dataset com o histórico do movimento diário do R.U', CURRENT_DATE, '12345678910');

INSERT INTO feature_store.versao (arquivo_path, numero_versao, data_inclusao, id_dataset, id_versao_base) VALUES ('./', '1', CURRENT_DATE, 1, NULL);

INSERT INTO feature_store.versao (arquivo_path, numero_versao, data_inclusao, id_dataset, id_versao_base) VALUES ('./', '1.1', CURRENT_DATE, 1, 1);

INSERT INTO feature_store.feature (nome, tipo_dado, descricao, id_versao) VALUES ('quantidade_alunos', 'INT', 'Total de pessoas no dia', 2), ('data_acesso', 'DATE', 'Data de registro da catraca', 2);

INSERT INTO feature_store.log_acesso (tipo_operacao, data_hora, cpf_usuario, id_dataset) VALUES (1, CURRENT_TIMESTAMP, '12345678910', 1), (0, CURRENT_TIMESTAMP, '98765432101', 1);
