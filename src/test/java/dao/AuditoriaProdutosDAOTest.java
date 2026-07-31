package dao;

import db.ConnectionFactory;
import integration.IntegrationTestBase;
import domain.model.AuditoriaProdutos;
import domain.model.Produto;
import domain.model.StatusProduto;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AuditoriaProdutosDAOTest extends IntegrationTestBase {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();
    private final AuditoriaProdutosDAO auditoriaProdutosDAO = new AuditoriaProdutosDAO();

    @Test
    void deveRegistrarAuditoriaAoInserirProduto(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        List<AuditoriaProdutos> auditorias = auditoriaProdutosDAO.listar();

        assertEquals(1, auditorias.size());

        AuditoriaProdutos auditoria = auditorias.get(0);
        assertEquals("INSERT", auditoria.getOperacao());
        assertEquals("Monitor", auditoria.getNomeNovo());
        assertEquals(900.0, auditoria.getPrecoNovo());
        assertEquals(10, auditoria.getQuantidadeNova());
        assertEquals("ATIVO", auditoria.getStatusNovo());
        assertNotNull(auditoria.getUsuarioBanco());
    }

    @Test
    void deveRegistrarAuditoriaAoAtualizarProduto(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        produtoDAO.atualizar(1, 750.00);

        List<AuditoriaProdutos> auditorias =  auditoriaProdutosDAO.listar();

        assertEquals(2, auditorias.size());

        AuditoriaProdutos auditoriaUpdate = auditorias.stream()
                .filter(a -> a.getOperacao().equals("UPDATE"))
                .findFirst()
                .orElseThrow();

        assertEquals("Monitor", auditoriaUpdate.getNomeAntigo());
        assertEquals("Monitor", auditoriaUpdate.getNomeNovo());
        assertEquals(900.0, auditoriaUpdate.getPrecoAntigo());
        assertEquals(750.0, auditoriaUpdate.getPrecoNovo());
    }

    @Test
    void deveRegistrarAuditoriaAoDeletarProduto() throws SQLException {
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        try (Connection connection =
                     ConnectionFactory.getConnection();

             PreparedStatement statement =
                     connection.prepareStatement("DELETE FROM produtos WHERE id = ?")
        ){
            statement.setInt(1, 1);
            statement.executeUpdate();
        }

        List<AuditoriaProdutos> auditorias = auditoriaProdutosDAO.listar();

        AuditoriaProdutos auditoriaDelete = auditorias.stream()
                .filter(a -> a.getOperacao().equals("DELETE"))
                .findFirst()
                .orElseThrow();

        assertEquals("Monitor", auditoriaDelete.getNomeAntigo());
        assertEquals(900.0, auditoriaDelete.getPrecoAntigo());
        assertEquals(10, auditoriaDelete.getQuantidadeAntiga());
        assertEquals("ATIVO", auditoriaDelete.getStatusAntigo());
    }
}