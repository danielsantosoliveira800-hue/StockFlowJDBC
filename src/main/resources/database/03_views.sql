-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 03_views.sql
-- Descrição: Criação das Views utilizadas para relatórios e dashboard.
-- Autor: Daniel S. Oliveira
-- ==========================================================

DROP VIEW IF EXISTS vw_historico_movimentacoes;
DROP VIEW IF EXISTS vw_ranking_produtos;

-- View: histórico de movimentações com nome do produto já resolvido
-- (evita repetir o JOIN produtos/movimentacoes em cada relatório)
CREATE VIEW vw_historico_movimentacoes AS
SELECT
    m.id                 AS id,
    m.produto_id         AS produto_id,
    p.nome               AS nome_produto,
    m.tipo               AS tipo,
    m.quantidade         AS quantidade,
    m.data_movimentacao  AS data_movimentacao
FROM movimentacoes m
        INNER JOIN produtos p ON m.produto_id = p.id
ORDER BY m.data_movimentacao DESC;


-- View: ranking de produtos por volume de movimentação (giro de estoque)
CREATE VIEW vw_ranking_produtos AS
SELECT
    p.id                AS id,
    p.nome              AS nome,
    COUNT(*)            AS total_movimentacoes,
    SUM(m.quantidade)   AS quantidade_movimentada
FROM movimentacoes m
         INNER JOIN produtos p ON m.produto_id = p.id
GROUP BY p.id, p.nome
ORDER BY total_movimentacoes DESC;