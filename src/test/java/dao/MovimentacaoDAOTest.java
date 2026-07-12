package dao;

import integration.IntegrationTestBase;
import model.Movimentacao;
import model.Produto;
import model.StatusProduto;
import model.TipoMovimentacao;
import db.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MovimentacaoDAOTest extends IntegrationTestBase{

    private final MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Test
    void deveListarHistoricoDeMovimentacoesComNomeDoProduto() throws SQLException {
        produtoDAO.salvarProduto(
                new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        try (Connection connection = ConnectionFactory.getConnection()){
            movimentacaoDAO.registrarMovimentacao(connection,
                    new Movimentacao(1, TipoMovimentacao.ENTRADA, 10));
        }

        List<Movimentacao> historico = movimentacaoDAO.listar();

        assertEquals(1, historico.size());
        assertEquals("Monitor", historico.get(0).getNomeProduto());
        assertEquals(TipoMovimentacao.ENTRADA, historico.get(0).getTipo());
        assertEquals(10, historico.get(0).getQuantidade());
    }

    @Test
    void deveBuscarMovimentacoesPorPeriodo() throws SQLException {
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        try (Connection connection = ConnectionFactory.getConnection()) {
            movimentacaoDAO.registrarMovimentacao(connection, new Movimentacao(1, TipoMovimentacao.ENTRADA, 10));
        }

        LocalDate hoje = LocalDate.now();
        List<Movimentacao> resultado = movimentacaoDAO.buscarPorPeriodo(hoje, hoje);

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNomeProduto());
    }
}
