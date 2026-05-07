# Trabalho_Banco_de_Dados_2026
Repositório dedicado ao trabalho de Banco de dados I do 5º semestre do curso de Ciências da Computação na UEL. Resumo: A proposta de trabalho é desenvolver um sistema que permita salvar e recuperar datasets de features e apresente relatórios e gráficos com consultas sobre esses datasets.

Middleware de Feature Store
Este projeto consiste no desenvolvimento de um middleware sobre um Sistema Gerenciador de Banco de Dados (SGBD) relacional que implementa funcionalidades essenciais de uma Feature Store. O sistema é voltado para o gerenciamento, versionamento e análise de datasets de features utilizados em tarefas de Machine Learning.

O objetivo principal é a aplicação prática de conceitos de bancos de dados, arquitetura de software em camadas e integração de aplicações web, seguindo rigorosas boas práticas de desenvolvimento.

🎯 Objetivos
Implementar a integração de bancos de dados em aplicações web multicamadas.

Praticar o desenvolvimento de sistemas sem o uso de abstrações de alto nível para persistência (sem JPA/Hibernate).

Explorar recursos avançados da linguagem SQL para geração de relatórios e estatísticas.

Garantir a integridade e linhagem dos dados em um ambiente versionado.

🚀 Funcionalidades
Gerenciamento de Datasets e Usuários
Cadastro e autenticação de usuários.

Submissão de datasets em formato CSV.

Registro detalhado de metadados: descrição, fontes de dados e descrição de cada feature.

Registro automático de autoria e carimbo de tempo (timestamp) de inclusão.

Versionamento e Linhagem (Data Lineage)
Suporte a modificações de datasets (tratamento e preparação de dados).

Controle de versões similar ao Git: cada nova versão registra sua "versão base".

Histórico de transformações: registro de inclusão, remoção ou transformação de features.

Download de qualquer versão histórica do dataset.

Relatórios e Análises
Listagem completa de datasets disponíveis.

Visualização detalhada de metadados e árvore de linhagem.

Dashboard Estatístico: Gráficos e tabelas baseados em consultas SQL complexas, incluindo:

Total de datasets e versões.

Rankings de datasets mais visualizados ou baixados.

Histórico temporal de acessos e downloads.

Estatísticas de uso por dataset específico.

🛠️ Tecnologias e Restrições
Para atender aos requisitos pedagógicos, o projeto segue as seguintes especificações:

Back-end: Java / J2EE.

Front-end: JavaScript (Sugestão: React) ou JSP/XHTML.

Persistência: SGBD Relacional com dados normalizados.

Arquitetura: Padrão em camadas utilizando Controllers, Models e Data Access Objects (DAO).

Restrição de Framework: É proibido o uso de implementações JPA (como Hibernate). Toda a comunicação com o banco deve ser feita via JDBC ou similar, focando na escrita manual de SQL.

📊 Requisitos de Banco de Dados
Os relatórios do sistema exploram profundamente a linguagem SQL, utilizando:

Junções (Internal e External Joins).

Agregações e Agrupamentos (GROUP BY, COUNT, SUM, etc.).

Subconsultas e Filtros Avançados.

Funções de Ranking e Ordenação.

👥 Desenvolvimento e Avaliação
O projeto é desenvolvido em conformidade com as seguintes diretrizes:

Trabalho em Grupo: Máximo de 2 alunos.

Versionamento: O histórico de commits no Git é parte fundamental da avaliação, demonstrando a evolução constante e o equilíbrio de contribuições entre os membros do grupo.

Como executar o projeto (Exemplo de Placeholder)
Pré-requisitos:

Java JDK 11+

Servidor de Aplicação (Tomcat/Glassfish)

Banco de Dados PostgreSQL/MySQL

Configuração:

Clone o repositório.

Configure o arquivo de conexão com o banco em src/main/resources/...

Execução:

Compile o projeto usando [Maven/Ant].

Inicie o servidor e acesse localhost:8080.

Curso de Ciência da Computação – Universidade Estadual de Londrina (UEL)
