-- ==========================================================
-- Projeto: StockFlowJDBC
-- Arquivo: 06_triggers.sql
-- Descrição: Triggers da tabela produtos.
--            - BEFORE: sincronizam o status com a quantidade
--              automaticamente, antes de salvar (INSERT/UPDATE).
--            - AFTER: registram em auditoria_produtos toda
--              criação, alteração e exclusão de produtos.
-- Autor: Daniel S. Oliveira
-- ==========================================================

DROP TRIGGER IF EXISTS trg_produtos_before_insert;
DROP TRIGGER IF EXISTS trg_produtos_before_update;
DROP TRIGGER IF EXISTS trg_produtos_after_insert;
DROP TRIGGER IF EXISTS trg_produtos_after_update;
DROP TRIGGER IF EXISTS trg_produtos_after_delete;

DELIMITER $$

-- Trigger: garante que o status reflita a quantidade
-- já no momento da inserção (não depende de nenhum job externo)
CREATE TRIGGER trg_produtos_before_insert
    BEFORE INSERT ON produtos
    FOR EACH ROW
BEGIN
    IF NEW.quantidade = 0 THEN
        SET NEW.status = 'INATIVO';
    ELSE
        SET NEW.status = 'ATIVO';
END IF;
END $$


-- Trigger: garante que o status reflita a quantidade
-- sempre que o produto for atualizado
CREATE TRIGGER trg_produtos_before_update
    BEFORE UPDATE ON produtos
    FOR EACH ROW
BEGIN
    IF NEW.quantidade = 0 THEN
        SET NEW.status = 'INATIVO';
    ELSE
        SET NEW.status = 'ATIVO';
END IF;
END $$


-- Trigger: registra em auditoria_produtos a criação de um novo produto
CREATE TRIGGER trg_produtos_after_insert
    AFTER INSERT ON produtos
    FOR EACH ROW
BEGIN
    INSERT INTO auditoria_produtos (
        produto_id,
        operacao,
        nome_novo,
        preco_novo,
        quantidade_nova,
        status_novo,
        usuario_banco
    )
    VALUES (
               NEW.id,
               'INSERT',
               NEW.nome,
               NEW.preco,
               NEW.quantidade,
               NEW.status,
               CURRENT_USER()
           );
END $$


-- Trigger: registra em auditoria_produtos o estado "antes" e "depois"
-- de cada UPDATE em produtos (rastreabilidade de alterações)
CREATE TRIGGER trg_produtos_after_update
    AFTER UPDATE ON produtos
    FOR EACH ROW
BEGIN
    INSERT INTO auditoria_produtos (
        produto_id,
        operacao,
        nome_antigo,
        nome_novo,
        preco_antigo,
        preco_novo,
        quantidade_antiga,
        quantidade_nova,
        status_antigo,
        status_novo,
        usuario_banco
    )
    VALUES (
               OLD.id,
               'UPDATE',
               OLD.nome,
               NEW.nome,
               OLD.preco,
               NEW.preco,
               OLD.quantidade,
               NEW.quantidade,
               OLD.status,
               NEW.status,
               CURRENT_USER()
           );
END $$


-- Trigger: registra em auditoria_produtos o estado final de um
-- produto antes de ser excluído (preserva histórico pós-exclusão)
-- Observação: auditoria_produtos.produto_id NÃO possui Foreign Key
-- para produtos.id de forma intencional — essa trigger insere o
-- registro de auditoria DEPOIS que o produto já foi removido,
-- então uma FK aqui causaria violação de constraint.
CREATE TRIGGER trg_produtos_after_delete
    AFTER DELETE ON produtos
    FOR EACH ROW
BEGIN
    INSERT INTO auditoria_produtos (
        produto_id,
        operacao,
        nome_antigo,
        preco_antigo,
        quantidade_antiga,
        status_antigo,
        usuario_banco
    )
    VALUES (
               OLD.id,
               'DELETE',
               OLD.nome,
               OLD.preco,
               OLD.quantidade,
               OLD.status,
               CURRENT_USER()
           );
END $$

DELIMITER ;

-- Fim do arquivo 06_triggers.sql