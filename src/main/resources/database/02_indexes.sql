-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 02_indexes.sql
-- Descrição: Criação dos índices para otimização das consultas.
-- Autor: Daniel S. Oliveira
-- ==========================================================

-- Acelera buscas/filtros de produto por nome (ex: tela de busca)
CREATE INDEX idx_produto_nome
    ON produtos(nome);

-- Acelera filtros de produtos por faixa de quantidade
-- (ex: relatório de itens com estoque baixo)
CREATE INDEX idx_produto_quantidade
    ON produtos(quantidade);

-- Acelera consultas por período em movimentações
-- (ex: vw_historico_movimentacoes ordenando por data)
CREATE INDEX idx_movimentacao_data
    ON movimentacoes(data_movimentacao);

-- Acelera busca do histórico de auditoria de um produto específico
CREATE INDEX idx_auditoria_produto_id
    ON auditoria_produtos(produto_id);

-- Acelera consultas/limpeza de auditoria por data
-- (ex: sp_limpar_auditoria_antiga)
CREATE INDEX idx_data_alteracao
    ON auditoria_produtos(data_alteracao);