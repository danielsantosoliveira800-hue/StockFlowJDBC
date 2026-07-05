package dao;

import db.ConnectionFactory;
import exception.PersistenciaException;
import model.Produto;
import model.ProdutoRanking;
import model.ResumoEstoque;
import model.StatusProduto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO implements ProdutoRepository {

    @Override
    public void salvarProduto(Produto produto) {

        String sql =
                "INSERT INTO produtos " +
                        "(nome, preco, quantidade, status) " +
                        "VALUES (?, ?, ?, ?)";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setString(4, produto.getStatus().name());

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao salvar produto.",e);
        }
    }

    @Override
    public List<Produto> listar() {

        String sql = "SELECT * FROM PRODUTOS";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()) {
            List<Produto> produtos = new ArrayList<>();
            while (resultSet.next()) {

                Produto produto =
                        mapearProduto(resultSet);

                produtos.add(produto);
            }

            return produtos;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao listar produto.",e);
        }
    }

    @Override
    public void atualizar(int id, double novoPreco) {

        String sql = " UPDATE produtos " +
                "SET preco = ? " +
                "WHERE id = ? ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDouble(1, novoPreco);

            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar produto.",e);
        }
    }

    @Override
    public void desativar(int id) {

        String sql = "UPDATE produtos"+
                     "SET status = 'INATIVO' "+
                     "WHERE id = ?";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new PersistenciaException("Erro ao desativar produto.",e);
        }
    }

    @Override
    public Produto buscar(Connection connection, int id) {
        String sql =
                "SELECT * FROM produtos " +
                        "WHERE id = ? ";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);) {
            statement.setInt(1, id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {
                return mapearProduto(resultSet);

            }

            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar produto.",e);
        }
    }

    @Override
    public Produto buscar(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (
                Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapearProduto(resultSet);
            }

            return null;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar produto.",e);
        }
    }

    @Override
    public List<Produto> buscarPorNome(String nomeBusca) {

        String sql =
                "SELECT * FROM produtos " +
                        "WHERE nome LIKE ?";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)

        ) {

            statement.setString(1, "%" + nomeBusca + "%");

            ResultSet resultSet =
                    statement.executeQuery();

            List<Produto> produtos = new ArrayList<>();

            while (resultSet.next()) {
                produtos.add(mapearProduto(resultSet));
            }

            return produtos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar produto por nome.",e);
        }
    }

    @Override
    public void atualizarQuantidade(Connection connection, int id, int novaQuantidade) {
        String sql =
                "UPDATE produtos " +
                        "SET quantidade  = ? " +
                        "WHERE id = ? ";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setInt(1, novaQuantidade);

            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar quantidade.",e);
        }
    }

    @Override
    public List<Produto> buscarEstoqueBaixo() {
        String sql = "SELECT * FROM produtos " +
                "WHERE quantidade <= 5 ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
        ) {
            List<Produto> produtos = new ArrayList<>();
            while (resultSet.next()) {

                Produto produto = mapearProduto(resultSet);

                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar estoque baixo.",e);
        }
    }

    @Override
    public List<Produto> buscarProdutosAtivos() {
        String sql =
                "SELECT * FROM produtos " +
                        "WHERE status = 'ATIVO' ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
        ) {
            List<Produto> produtos = new ArrayList<>();
            while (resultSet.next()) {

                Produto produto = mapearProduto(resultSet);

                produtos.add(produto);
            }
            return produtos;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar produtos ativos.",e);
        }
    }

    @Override
    public double calcularValorTotalEstoque() {
        String sql = "SELECT SUM(preco * quantidade) AS total " +
                "FROM produtos ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
        ) {
            if (resultSet.next()) {
                return resultSet.getDouble("total");
            }

            return 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao calcular valor do estoque.",e);
        }
    }

    private Produto mapearProduto(ResultSet rs) throws SQLException {

        Produto produto = new Produto();

        produto.setId(rs.getInt("id"));
        produto.setNome(rs.getString("nome"));
        produto.setPreco(rs.getDouble("preco"));
        produto.setQuantidade(rs.getInt("quantidade"));
        produto.setStatus(StatusProduto.valueOf(rs.getString("status")));


        return produto;
    }

    @Override
    public int contaProdutos() {
        String sql =
                " SELECT COUNT(*) AS total " +
                        " FROM produtos ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
        ) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }

            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar produtos.",e);
        }
    }

    @Override
    public int contaProdutosAtivos() {
        String sql =
                "SELECT COUNT(*) AS total " +
                        "FROM produtos " +
                        "WHERE status = 'ATIVO' ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
        ) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar produtos ativos.",e);
        }
    }

    @Override
    public int contaProdutosInativos() {
        String sql =
                "SELECT COUNT(*) AS total " +
                        "FROM produtos " +
                        "WHERE status = 'INATIVO'";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();

        ) {
            if (resultSet.next()) {
                return resultSet.getInt("total");

            }
            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar produtos inativos.",e);
        }
    }

    @Override
    public int quantidadeTotalProdutos() {
        String sql =
                "SELECT SUM(quantidade) AS total " +
                        "FROM produtos ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();

        ) {
            if (resultSet.next()) {
                return resultSet.getInt("total");
            }
            return 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao contar o total de produtos.",e);
        }
    }

    @Override
    public List<ProdutoRanking> buscarRankingProdutos() {
        String sql =
                "SELECT * FROM  vw_ranking_produtos";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery()) {
            List<ProdutoRanking> rankingDeProdutos = new ArrayList<>();

            while (resultSet.next()) {
                ProdutoRanking produtoRanking = new ProdutoRanking();

                produtoRanking.setNomeProduto(resultSet.getString("nome"));
                produtoRanking.setQuantidadeMovimentada(resultSet.getInt("quantidade_movimentada"));
                produtoRanking.setTotalMovimentacoes(resultSet.getInt("total_movimentacoes"));

                rankingDeProdutos.add(produtoRanking);
            }
            return rankingDeProdutos;
        } catch (SQLException e) {

            throw new PersistenciaException("Erro ao buscar ranking de produtos.",e);
        }
    }

    @Override
    public ResumoEstoque buscarResumoEstoque() {

        String sql = "{CALL sp_resumo_estoque()}";

        try (
                Connection connection = ConnectionFactory.getConnection();
                CallableStatement statement = connection.prepareCall(sql);
                ResultSet rs = statement.executeQuery();
        ) {

            if (rs.next()) {

                ResumoEstoque resumo = new ResumoEstoque();

                resumo.setTotalProdutos(rs.getInt("total_produtos"));
                resumo.setQuantidadeTotalProdutos(rs.getInt("quantidade_total"));
                resumo.setValorTotalEstoque(rs.getDouble("valor_total_estoque"));
                resumo.setProdutosAtivos(rs.getInt("produtos_ativos"));
                resumo.setProdutosInativos(rs.getInt("produtos_inativos"));

                return resumo;

            }
            return null;

        }catch (SQLException e){
            throw new PersistenciaException("Erro ao resumir estoque.",e);
        }
    }

    @Override
    public double calcularValorProduto(int id) {
        String sql = "{ ? =  call fn_calcular_valor_produto(?) }";

        try (
                Connection connection = ConnectionFactory.getConnection();

                CallableStatement statement = connection.prepareCall(sql);

                )
        {
            statement.registerOutParameter(1, Types.DECIMAL);

            statement.setInt(2, id);

            statement.execute();

            return statement.getDouble(1);

        }catch (SQLException e){
            throw new PersistenciaException("Erro ao calcular valor do produto.",e);
        }
    }

    @Override
    public void inserirProdutoEmLote(List<Produto> produtos) {
        String sql = """
                    INSERT INTO produtos (nome, preco, quantidade, status)
                    VALUES (?, ?, ?, ?)
                    """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(sql);

            for (Produto produto : produtos) {
                preencherStatement(statement,produto);
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    throw new PersistenciaException("Erro ao executar rollback do lote.",e);
                }
            }
            throw new PersistenciaException("Erro ao inserir produtos em lote.",e);

        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                throw new PersistenciaException("Erro ao inserir produto em lote.",e);
            }
        }
    }

    @Override
    public void inserirProdutosComSavepoint(List<Produto> produtos) {

        String sql = """
                insert into produtos(nome, preco, quantidade, status)
                values (?, ?, ?, ?)
                """;

        Connection connection= null;
        PreparedStatement statement = null;
        Savepoint savepoint = null;

        try {

            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            System.out.println("começando transação.");
            statement = connection.prepareStatement(sql);

            int contador = 0;

            for (Produto produto : produtos) {

                preencherStatement(statement,produto);

                System.out.println("Inserindo produto: "+ produto.getNome());
                statement.executeUpdate();
                contador++;

                if (contador == 2){
                    savepoint =connection.setSavepoint("PONTO_SEGURO");
                }

                if (contador == 4){
                    throw new SQLException("Erro simulado para teste");
                }
                
            }

            connection.commit();
            System.out.println("Todos os produtos foram cadastrados com sucesso.");

        }catch (SQLException e){
            try {
                if (connection !=  null && savepoint != null){
                    connection.rollback(savepoint);
                    System.out.println("Rolback realizado com savePoint.");
                    connection.commit();
                    System.out.println("commit realizado.");
                }else {
                    connection.rollback();
                    System.out.println("Roolback total. ");
                }
            }catch (SQLException e1){
                throw new PersistenciaException("Erro rollback com savenpoint.",e1);
            }
        }finally {
            try {
                if (statement != null){
                    statement.close();
                }
                if (connection != null){
                    connection.setAutoCommit(true);
                    connection.close();
                }
            }catch (SQLException e){
                throw new PersistenciaException("Erro ao inserir produto com savePoint.",e);
            }
        }
    }

    private void preencherStatement(PreparedStatement statement, Produto produto) throws SQLException {

        statement.setString(1, produto.getNome());
        statement.setDouble(2,produto.getPreco());
        statement.setInt(3,produto.getQuantidade());
        statement.setString(4,produto.getStatus().name());

    }
}