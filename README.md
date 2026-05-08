# 📊 Middleware de Feature Store

> **Repositório dedicado ao trabalho da disciplina de Banco de Dados I**
> 5º semestre – Ciência da Computação | Universidade Estadual de Londrina (UEL)

## 📖 Visão Geral

Este projeto consiste no desenvolvimento de um *middleware* sobre um Sistema Gerenciador de Banco de Dados (SGBD) relacional, implementando as funcionalidades essenciais de uma **Feature Store**. 

O sistema atua como um repositório centralizado voltado para o gerenciamento, versionamento e análise de *datasets* de *features* utilizados no treinamento de modelos de *Machine Learning*. O objetivo central é a aplicação prática de modelagem de dados, arquitetura de software em camadas e integração de aplicações web, seguindo rigorosas boas práticas de engenharia de software.

---

## 🎯 Objetivos de Aprendizado

*   **Integração:** Conectar de forma eficiente bancos de dados relacionais em aplicações web multicamadas.
*   **Persistência Nativa:** Praticar o desenvolvimento de sistemas sem abstrações de alto nível (ausência total de JPA/Hibernate).
*   **Domínio de SQL:** Explorar recursos e operadores avançados da linguagem SQL para a geração de métricas e relatórios gerenciais.
*   **Governança de Dados:** Garantir a integridade, o rastreamento e a linhagem (*Data Lineage*) em um ambiente versionado.

---

## 🚀 Escopo Funcional

As funcionalidades do sistema estão divididas nos seguintes módulos centrais:

### 👤 1. Gestão de Acesso e Autenticação
*   Cadastro de novos usuários no sistema.
*   Autenticação segura mediante credenciais validadas.
*   Registro automático de autoria e carimbo de tempo (*timestamp*) atrelado às ações dos usuários logados.

### 📁 2. Gerenciamento de Datasets (CRUD)
*   Submissão e inserção de novos *datasets* base em formato `.csv`.
*   Registro detalhado de metadados: descrição do projeto, fontes de origem dos dados e descrição individual e opcional de cada *feature*.
*   Listagem em catálogo de todos os *datasets* disponíveis.
*   Exclusão de *datasets* (gerenciando a integridade referencial).

### 🌳 3. Versionamento e Linhagem (Data Lineage)
*   Suporte à criação de novas versões derivadas de um *dataset* original (após tratamento e preparação dos dados).
*   Controle de versões estruturado: cada nova entrada registra obrigatoriamente a sua "versão base".
*   Registro histórico de transformações (descrição completa sobre inclusão, remoção ou modificação de *features*).
*   Visualização da árvore de linhagem do *dataset* selecionado.
*   Possibilidade de *download* do arquivo CSV de qualquer versão histórica.

### 📈 4. Monitoramento e Analytics
*   Registro silencioso de data, hora e usuário para todos os *downloads* e acessos realizados.
*   **Dashboard Estatístico:** Geração de relatórios visuais (gráficos e tabelas) baseados no comportamento do sistema, incluindo:
    *   Métricas globais (total de *datasets* e de versões armazenadas).
    *   Rankings de popularidade (arquivos mais visualizados ou baixados).
    *   Histórico temporal de acessos e *downloads*.
    *   Estatísticas isoladas de uso por *dataset* específico.

---

## 🛠️ Tecnologias e Restrições Arquiteturais

Para atender aos requisitos pedagógicos da disciplina, a arquitetura obedece às seguintes especificações:

*   **Back-end:** Java / J2EE.
*   **Front-end:** JavaScript (Sugestão: React) ou JSP/XHTML.
*   **Persistência:** SGBD Relacional com esquema de dados estritamente normalizado (Sugestão: PostgreSQL ou MySQL).
*   **Arquitetura:** Padrão estrutural em camadas (*Controllers*, *Models* e *Data Access Objects - DAO*).

---

## 📊 Requisitos de Complexidade em SQL

Os módulos de relatórios e *dashboards* foram projetados para explorar profundamente a linguagem SQL. O sistema deve fazer uso obrigatório e variado de:

*   **Junções:** Consultas envolvendo *Internal Joins* e *External Joins*.
*   **Agrupamentos e Agregações:** Uso de `GROUP BY` integrado com funções como `COUNT`, `SUM`, `AVG`, etc.
*   **Aninhamentos:** Aplicação de subconsultas (subqueries) e filtros complexos condicionados.
*   **Ordenação Avançada:** Utilização de funções de *Ranking* e estruturações temporais.

---

## ⚙️ Configuração e Execução (Em Breve)

*Esta seção será populada com os scripts de inicialização, criação do banco de dados e comandos de execução dos servidores assim que a estrutura base do código for implementada.*

**Pré-requisitos básicos esperados:**
*   Java JDK 11+
*   Servidor de Aplicação (Tomcat/Glassfish)
*   SGBD (PostgreSQL/MySQL) ativo localmente
