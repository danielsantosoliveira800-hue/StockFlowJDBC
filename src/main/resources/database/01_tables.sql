-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 01_tables.sql
-- Descrição: Criação das tabelas principais do banco de dados.
-- Autor: Daniel S. Oliveira
-- ==========================================================

DROP TABLE IF EXISTS auditoria_produtos;
DROP TABLE IF EXISTS movimentacoes;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS historico_dashboard;


-- Tabela: produtos
-- Cadastro principal do estoque
CREATE TABLE produtos (
                          id          INT NOT NULL AUTO_INCREMENT,
                          nome        VARCHAR(100) NOT NULL,
                          preco       DECIMAL(10,2) NOT NULL,
                          quantidade  INT NOT NULL,
                          status      VARCHAR(20) NOT NULL,

                          PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- Tabela: movimentacoes
-- Registra entradas/saídas de estoque por produto
CREATE TABLE movimentacoes (
                               id                  INT NOT NULL AUTO_INCREMENT,
                               produto_id          INT NOT NULL,
                               tipo                VARCHAR(20) NOT NULL,
                               quantidade          INT NOT NULL,
                               data_movimentacao   TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,

                               PRIMARY KEY (id),
                               KEY produto_id (produto_id),
                               CONSTRAINT movimentacoes_ibfk_1
                                   FOREIGN KEY (produto_id) REFERENCES produtos (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- Tabela: auditoria_produtos
-- Histórico de alterações (antes/depois) feito via Triggers
CREATE TABLE auditoria_produtos (
                                    id                  INT NOT NULL AUTO_INCREMENT,
                                    produto_id          INT NOT NULL,
                                    operacao            VARCHAR(10) NOT NULL,
                                    nome_antigo         VARCHAR(255) DEFAULT NULL,
                                    nome_novo           VARCHAR(255) DEFAULT NULL,
                                    preco_antigo        DECIMAL(10,2) DEFAULT NULL,
                                    preco_novo          DECIMAL(10,2) DEFAULT NULL,
                                    quantidade_antiga   INT DEFAULT NULL,
                                    quantidade_nova     INT DEFAULT NULL,
                                    status_antigo       VARCHAR(20) DEFAULT NULL,
                                    status_novo         VARCHAR(20) DEFAULT NULL,
                                    usuario_banco       VARCHAR(100) DEFAULT NULL,
                                    data_alteracao      DATETIME DEFAULT CURRENT_TIMESTAMP,

                                    PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;


-- Tabela: historico_dashboard
-- Armazena snapshots periódicos do resumo de estoque,
-- permitindo acompanhar a evolução do dashboard ao longo do tempo
CREATE TABLE historico_dashboard (
                                     id                   INT NOT NULL AUTO_INCREMENT,
                                     total_produtos       INT NOT NULL,
                                     produtos_ativos      INT NOT NULL,
                                     produtos_inativos    INT NOT NULL,
                                     quantidade_total     INT NOT NULL,
                                     valor_total_estoque  DECIMAL(15,2) NOT NULL,
                                     data_snapshot        DATETIME DEFAULT CURRENT_TIMESTAMP,

                                     PRIMARY KEY (id)
)
    ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_0900_ai_ci;