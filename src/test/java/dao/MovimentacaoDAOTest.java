package dao;

import infrasctructure.persistence.MovimentacaoDAO;
import infrasctructure.persistence.ProdutoDAO;
import integration.IntegrationTestBase;
import domain.model.Movimentacao;
import domain.model.Produto;
import domain.model.StatusProduto;
import domain.model.TipoMovimentacao;
import infrasctructure.ConnectionFactory;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import exception.PersistenciaException;
import org.slf4j.LoggerFactory;

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

    @Test
    void deveCalcularValorDeUmProdutoViaFunction(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        double valor = produtoDAO.calcularValorProduto(1);

        assertEquals(9000.0, valor);
    }

    @Test
    void deveLogarErroAoRegistrarMovimentacaoComProdutoInexistente() throws SQLException {
        Logger erroLogger = (Logger) LoggerFactory.getLogger(MovimentacaoDAO.class);
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        erroLogger.addAppender(listAppender);

        Movimentacao movimentacao = new Movimentacao(9999, TipoMovimentacao.SAIDA, 8000);

        try (
                Connection connection = ConnectionFactory.getConnection()){
            assertThrows(PersistenciaException.class,
                    () -> movimentacaoDAO.registrarMovimentacao(connection, movimentacao));
        }

        boolean encontrouLogErro = listAppender.list.stream()
                .anyMatch(evento -> evento.getLevel() == Level.ERROR);

        assertTrue(encontrouLogErro);
        erroLogger.detachAppender(listAppender);
    }
}
