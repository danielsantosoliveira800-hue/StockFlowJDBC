-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 05_procedures.sql
-- Descrição: Criação das Stored Procedures utilizadas pelo sistema.
-- Autor: Daniel S. Oliveira
-- ==========================================================

DROP PROCEDURE IF EXISTS inserir_produtos_teste;
DROP PROCEDURE IF EXISTS sp_resumo_estoque;
DROP PROCEDURE IF EXISTS sp_sincronizar_status_produtos;
DROP PROCEDURE IF EXISTS sp_snapshot_dashboard;

DELIMITER $$

-- Procedure: popula a tabela produtos com 1000 registros fictícios
-- (usada apenas em ambiente de desenvolvimento/teste, para gerar
--  volume de dados e testar performance de índices e relatórios)
CREATE PROCEDURE inserir_produtos_teste()
BEGIN
    DECLARE i INT DEFAULT 1;

    WHILE i <= 1000 DO
        INSERT INTO produtos (nome, preco, quantidade, status)
        VALUES (
            CONCAT('Produto Teste ', i),
            ROUND(RAND() * 1000, 2),
            FLOOR(RAND() * 100),
            IF(RAND() > 0.5, 'ATIVO', 'INATIVO')
        );
        SET i = i + 1;
END WHILE;
END $$


-- Procedure: resumo geral do estoque (totais e valor financeiro)
-- usada para alimentar o dashboard com uma visão consolidada
CREATE PROCEDURE sp_resumo_estoque()
BEGIN
SELECT
    COUNT(*)                                              AS total_produtos,
    SUM(quantidade)                                       AS quantidade_total,
    ROUND(SUM(preco * quantidade), 2)                     AS valor_total_estoque,
    SUM(CASE WHEN status = 'ATIVO'   THEN 1 ELSE 0 END)    AS produtos_ativos,
    SUM(CASE WHEN status = 'INATIVO' THEN 1 ELSE 0 END)    AS produtos_inativos
FROM produtos;
END $$


-- Procedure: sincroniza manualmente o status do produto com a quantidade
-- real (estoque zerado -> INATIVO / estoque positivo -> ATIVO).
-- Uso: ferramenta de contingência/manutenção manual — a sincronização
-- automática é feita pelas triggers BEFORE INSERT/UPDATE.
CREATE PROCEDURE sp_sincronizar_status_produtos()
BEGIN
UPDATE produtos
SET status = 'INATIVO'
WHERE quantidade = 0
  AND status <> 'INATIVO';

UPDATE produtos
SET status = 'ATIVO'
WHERE quantidade > 0
  AND status <> 'ATIVO';
END $$


-- Procedure: grava um snapshot do resumo do estoque na tabela
-- historico_dashboard, permitindo análise de evolução ao longo
-- do tempo (ex: gráfico de valor total em estoque por dia)
CREATE PROCEDURE sp_snapshot_dashboard()
BEGIN
INSERT INTO historico_dashboard (
    total_produtos,
    produtos_ativos,
    produtos_inativos,
    quantidade_total,
    valor_total_estoque
)
SELECT
    COUNT(*),
    SUM(CASE WHEN status = 'ATIVO'   THEN 1 ELSE 0 END),
    SUM(CASE WHEN status = 'INATIVO' THEN 1 ELSE 0 END),
    SUM(quantidade),
    SUM(preco * quantidade)
FROM produtos;
END $$

DELIMITER ;

-- Fim do arquivo 05_procedures.sql