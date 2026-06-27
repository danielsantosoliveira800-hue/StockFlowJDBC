-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 07_events.sql
-- Descrição: Agendamento automático de procedures de manutenção
--            e snapshot do estoque.
-- Autor: Daniel S. Oliveira
-- ==========================================================
-- IMPORTANTE: Events só funcionam com o agendador global ativo.
-- Se necessário, execute antes:
-- SET GLOBAL event_scheduler = ON;
-- ==========================================================

DROP EVENT IF EXISTS ev_limpeza_auditoria;
DROP EVENT IF EXISTS ev_snapshot_dashboard;

-- Event: limpa registros de auditoria com mais de 1 ano
CREATE EVENT ev_limpeza_auditoria
    ON SCHEDULE EVERY 1 DAY
    STARTS CURRENT_TIMESTAMP
    ON COMPLETION NOT PRESERVE
    ENABLE
DO
    CALL sp_limpar_auditoria_antiga();


-- Event: grava snapshot diário do resumo de estoque em historico_dashboard
CREATE EVENT ev_snapshot_dashboard
    ON SCHEDULE EVERY 1 DAY
    STARTS CURRENT_TIMESTAMP
    ON COMPLETION NOT PRESERVE
    ENABLE
DO
    CALL sp_snapshot_dashboard();

-- Fim do arquivo 07_events.sql