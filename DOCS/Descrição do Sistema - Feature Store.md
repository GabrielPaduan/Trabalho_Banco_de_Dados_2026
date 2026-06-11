# Descrição do Sistema - Feature Store

## Visão Geral
A criação de um middleware sobre um banco de dados relacional é o ponto de partida para desenvolver um protótipo de Feature Store que será um repositório centralizado para armazenamento, versionamento e catalogação de "datasets" de features que são utilizados em modelos de Machine Learning. O foco primário é no quesito acadêmico aprimorando o conhecimento na área abrangendo aplicações web multicamadas e implementação de SQL em sua totalidade sem o uso de ORM.

---

## Ator - Usuário
Dado as características do sistema (protótipo) será aplicado um ator único denominado usuário. Esse ator, ao estar devidamente validado, poderá interagir com o sistema em sua totalidade, podendo visualizar e editar os "datasets". Suas responsabilidades abrangem:

* Inserir novos "datasets";
* Inserir novas versões de um dataset já criado;
* Visualizar os "datasets" inseridos por ele e sua linhagem de versões;
* Acessar os dashboards e relatórios criados.

---

## Escopo Funcional
Funcionalidades básicas acerca do protótipo desenvolvido:

### Acesso
* Realizar o login de usuário a partir de credenciais validadas;
* Realizar o cadastro de um usuário com nome, email e senha.

### CRUD do Dataset
* Criação (inserção) de "datasets";
* Visualização da listagem dos "datasets";
* Excluir (Desativar) um dataset.

### CRUD do Usuário
* Criação (inserção) de "usuários";
* Exclusão de um usuário.

### Gestão do Versionamento e Linhagem
* Criação de novas versões;
* Edições (novas versões) de um dataset com a descrição completa das modificações;
* Listagem da árvore de versões de um dataset ao selecioná-lo;
* Possibilidade de download de qualquer versão.

### Monitoramento
* Registro da data e hora do salvamento da nova versão juntamente com os dados;
* Registro da data, hora e do usuário que fez o download de uma versão do dataset.

### Relatório e Gráficos
* Geração de dashboards (gráficos e tabelas) referentes ao gerenciamento dos "datasets";
* Registro e visualização de métricas globais;
* Visualização de ranking dos "datasets";
* Histórico de acessos e downloads dos "datasets".
