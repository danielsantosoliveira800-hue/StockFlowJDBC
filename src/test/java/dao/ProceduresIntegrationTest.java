package dao;

import db.ConnectionFactory;
import integration.IntegrationTestBase;
import model.Produto;
import model.StatusProduto;
import org.junit.jupiter.api.Test;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProceduresIntegrationTest extends IntegrationTestBase {
    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Test
    void deveSincronizarStatusDosProdutos() throws SQLException {
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 0, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 10, StatusProduto.INATIVO));

        try (Connection connection = ConnectionFactory.getConnection()){
            CallableStatement statement = connection.prepareCall("{call sp_sincronizar_status_produtos()}");

            statement.execute();
        }

        assertEquals(StatusProduto.INATIVO, produtoDAO.buscar(1).getStatus());
        assertEquals(StatusProduto.ATIVO, produtoDAO.buscar(2).getStatus());
    }

    @Test
    void deveInserirProdutosDeTesteViaProcedure() throws SQLException {
        try (Connection connection = ConnectionFactory.getConnection();
             CallableStatement statement = connection.prepareCall("{call inserir_produtos_teste()}")) {

            statement.execute();
        }

        assertEquals(1000, produtoDAO.contaProdutos());
    }

    @Test
    void deveGravarSnapshotDoDashboard() throws SQLException {
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.INATIVO));

        try (Connection connection = ConnectionFactory.getConnection();
             CallableStatement statement = connection.prepareCall("{call sp_snapshot_dashboard()}")) {

            statement.execute();
        }

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement select = connection.prepareStatement("SELECT * FROM historico_dashboard");
             ResultSet resultSet = select.executeQuery()) {

            assertTrue(resultSet.next());
            assertEquals(2, resultSet.getInt("total_produtos"));
            assertEquals(1, resultSet.getInt("produtos_ativos"));
            assertEquals(1, resultSet.getInt("produtos_inativos"));
        }
    }
}
