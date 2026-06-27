 -- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 04_functions.sql
-- Descrição: Criação das Functions utilizadas pelo sistema.
-- Autor: Daniel S. Oliveira
-- ==========================================================

DROP FUNCTION IF EXISTS fn_calcular_valor_produto;

DELIMITER $$

-- Function: calcula o valor total em estoque de um produto
-- (preço unitário * quantidade) — usada em relatórios e dashboard
CREATE FUNCTION fn_calcular_valor_produto(
    p_produto_id INT
)
    RETURNS DECIMAL(15,2)
    DETERMINISTIC
    READS SQL DATA
BEGIN
    DECLARE valor_total DECIMAL(15,2);

SELECT preco * quantidade
INTO valor_total
FROM produtos
WHERE id = p_produto_id;

RETURN valor_total;
END $$

DELIMITER ;

-- Fim do arquivo 04_functions.sql