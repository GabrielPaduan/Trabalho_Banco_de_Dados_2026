SELECT * FROM feature_store.feature WHERE id_versao = 1;

SELECT v_filha.numero_versao AS versao_atual,
	   v_filha.arquivo_path, v_base.numero_versao AS derivada_da_versao
FROM feature_store.versao v_filha
LEFT JOIN feature_store.versao v_base ON v_filha.id_versao_base = v_base.id;

SELECT d.nome AS dataset, d.descricao, u.nome AS criador
FROM feature_store.dataset d
JOIN feature_store.usuario u ON d.cpf_usuario = u.cpf;