# StockFlow

Sistema de gerenciamento de estoque desenvolvido em Java utilizando JDBC puro e MySQL — sem frameworks como Spring, como exercício de fundamentos de backend.

## Sobre o Projeto

O StockFlow foi criado com o objetivo de aplicar, na prática, conceitos de Programação Orientada a Objetos, JDBC, SQL avançado, arquitetura em camadas, princípios SOLID, concorrência e observabilidade — construído de forma incremental, sprint a sprint, como base sólida antes de avançar para frameworks como Spring Boot.

A aplicação permite cadastrar produtos, controlar entradas e saídas de estoque, registrar movimentações, gerar relatórios e dashboards, exportar dados para CSV e manter um histórico de auditoria completo.

## Funcionalidades

### Produtos

* Cadastro de produtos
* Listagem de produtos
* Atualização de preços
* Desativação e reativação de produtos (manual ou automática por regra de estoque)
* Busca por ID
* Busca por nome
* Inserção em lote e inserção com savepoint (rollback parcial)

### Estoque e Movimentações

* Entrada e saída de estoque
* Registro automático de movimentações, com transação garantindo consistência
* Controle de concorrência (`SELECT ... FOR UPDATE`) para evitar perda de atualizações em vendas simultâneas
* Histórico completo de movimentações
* Consulta de movimentações por período

### Relatórios

* Dashboard com indicadores (total de produtos, ativos, inativos, valor em estoque)
* Ranking de produtos mais movimentados
* Produtos com estoque baixo
* Exportação de dados para CSV

### Auditoria

* Registro automático de todas as alterações em produtos (inserção, atualização, exclusão), via triggers no banco
* Consulta do histórico de auditoria pela aplicação

## Tecnologias Utilizadas

* Java 21
* JDBC puro (sem ORM)
* MySQL 8
* HikariCP (connection pool)
* Logback + SLF4J (logging estruturado)
* JUnit 5 + Mockito (testes unitários e de integração)
* Maven
* IntelliJ IDEA
* Git / GitHub

## Arquitetura

O projeto segue uma organização em camadas inspirada em Clean Architecture, aplicando princípios SOLID para reduzir acoplamento e manter cada parte do sistema com uma responsabilidade clara.

```text
src/main/java/
├── domain            # Modelos de negócio, interfaces de repositório e validadores
├── infrastructure    # DAOs (JDBC), conexão com o banco, agendador de tarefas
├── service           # Regras de aplicação e orquestração dos casos de uso
├── presentation      # Menu de console, dividido em submenus por categoria
├── util              # Utilitários sem estado (formatação, exportação CSV)
├── exception         # Exceções customizadas
└── main              # Ponto de entrada da aplicação
```

### Responsabilidades das camadas

* **domain** — entidades do sistema, contratos de persistência (interfaces) e regras de validação. Não depende de nenhuma outra camada.
* **infrastructure** — implementações concretas de acesso ao banco (JDBC puro), configuração de conexão (HikariCP) e tarefas agendadas em background.
* **service** — regras de negócio, depende apenas dos contratos definidos em `domain`, nunca de `infrastructure` diretamente.
* **presentation** — interação com o usuário via console, organizada em submenus (Produtos, Movimentações, Relatórios, Auditoria, Utilitários).
* **util** — formatação monetária e exportação de dados.

### Principais decisões de design

* **Dependency Inversion** — os DAOs recebem a conexão (`DataSource`) via construtor em vez de depender de uma classe estática concreta, permitindo testes de integração isolados sem configuração especial em tempo de execução.
* **Interface Segregation** — o acesso a dados de produto é dividido em interfaces menores e coesas (`ProdutoRepository`, `ProdutoConsultaRepository`, `ProdutoLoteRepository`, `ProdutoTransacionalRepository`), em vez de uma única interface acumulando responsabilidades diferentes.
* **Single Responsibility** — a lógica de produto é dividida em `ProdutoService` (CRUD e regras de negócio), `ProdutoRelatorioService` (agregações e relatórios) e `ProdutoLoteService` (inserção em lote e savepoints).

## Banco de Dados

O schema utiliza recursos avançados do MySQL, todos exercitados e testados na aplicação:

* **Views** — `vw_ranking_produtos`, `vw_historico_movimentacoes`
* **Stored Procedures** — `sp_resumo_estoque`, `sp_snapshot_dashboard`, `sp_sincronizar_status_produtos`, `sp_limpar_auditoria_antiga`, entre outras
* **Function** — `fn_calcular_valor_produto`
* **Triggers** — auditoria automática de `INSERT`, `UPDATE` e `DELETE` em produtos
* **Events** — limpeza automática de auditoria antiga
* **Índices** — otimização das consultas mais frequentes

### Recursos SQL utilizados

`SELECT`, `INSERT`, `UPDATE`, `DELETE`, `INNER JOIN`, `COUNT()`, `SUM()`, `GROUP BY`, `ORDER BY`, `BETWEEN`, `FOR UPDATE`

## Testes

O projeto tem uma suíte de testes em duas camadas:

* **Testes unitários** (Mockito) — cobrem `Service` e regras de negócio, com dependências mockadas
* **Testes de integração** — rodam contra um banco MySQL real e isolado (`stockflow_test`), cobrindo DAOs, views, procedures, triggers e o comportamento de concorrência

Os testes de integração já encontraram e corrigiram diversos bugs reais ao longo do desenvolvimento — incluindo erros de sintaxe SQL, filtros de data incorretos, uma condição de corrida (*lost update*) em vendas simultâneas, e inconsistências de log.

## Observabilidade

Logging estruturado com Logback, separado por canal:

* `app.log` — log geral da aplicação
* `error.log` — apenas erros
* `audit.log` — ações de negócio relevantes (cadastro, desativação, reativação de produtos)
* `transacoes.log` — início, commit e rollback de cada movimentação de estoque

Todos os arquivos têm rotação diária automática.

## Capturas de Tela

### Menu Principal

![Menu Principal](screenshots/menu-principal.png)

### Dashboard

![Dashboard](screenshots/dashboard.png)

### Ranking de Produtos

![Ranking de Produtos](screenshots/ranking-produtos.png)

## Projetos Relacionados

* [StockFlow Reports](https://github.com/danielsantosoliveira800-hue/stockflow-reports) — scripts em Python para exportação de relatórios em CSV, conectando ao mesmo banco de dados

## Autor

Daniel Santos Oliveira

GitHub: [https://github.com/danielsantosoliveira800-hue](https://github.com/danielsantosoliveira800-hue)