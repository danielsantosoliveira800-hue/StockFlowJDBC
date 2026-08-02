package infrasctructure.persistence;

import domain.repository.ProdutoConsultaRepository;
import domain.repository.ProdutoLoteRepository;
import domain.repository.ProdutoRepository;
import domain.repository.ProdutoTransacionalRepository;
import exception.PersistenciaException;
import domain.model.Produto;
import domain.model.ProdutoRanking;
import domain.model.ResumoEstoque;
import domain.model.StatusProduto;
import infrasctructure.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProdutoDAO implements ProdutoRepository,
        ProdutoConsultaRepository,
        ProdutoLoteRepository,
        ProdutoTransacionalRepository {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoDAO.class);
    private final DataSource dataSource;

    public ProdutoDAO(){
        this(ConnectionFactory.getDataSource());
    }

    public ProdutoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void salvarProduto(Produto produto) {

        String sql =
                "INSERT INTO produtos " +
                        "(nome, preco, quantidade, status, desativado_manualmente) " +
                        "VALUES (?, ?, ?, ?, ?)";

        try (
                Connection connection =
                        dataSource.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {

            statement.setString(1, produto.getNome());
            statement.setDouble(2, produto.getPreco());
            statement.setInt(3, produto.getQuantidade());
            statement.setString(4, produto.getStatus().name());
            statement.setBoolean(5, produto.isDesativadoManualmente());

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Erro ao salvar produto: {}", produto.getNome(), e);
            throw new PersistenciaException("Erro ao salvar produto.",e);
        }
    }

    @Override
    public List<Produto> listar() {

        String sql = "SELECT * FROM PRODUTOS";

        try (
                Connection connection =
                        dataSource.getConnection();

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
            logger.error("Erro ao listar produtos.", e);
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
                        dataSource.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)
        ) {

            statement.setDouble(1, novoPreco);

            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Erro ao atualizar produto id = {}: novo preço = {}", id, novoPreco, e);
            throw new PersistenciaException("Erro ao atualizar produto.",e);
        }
    }

    @Override
    public void desativar(int id) {

        String sql = "UPDATE produtos " +
                     "SET status = 'INATIVO', desativado_manualmente = TRUE " +
                     "WHERE id = ?";

        try (
                Connection connection =
                        dataSource.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            logger.error("Erro ao desativar produto id = {}", id, e);
            throw new PersistenciaException("Erro ao desativar produto.",e);
        }
    }

    @Override
    public Produto buscar(Connection connection, int id) {
        String sql = " SELECT * FROM produtos WHERE id = ? FOR UPDATE";

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
            logger.error("Erro ao buscar produto id = {}",id ,e );
            throw new PersistenciaException("Erro ao buscar produto.",e);
        }
    }

    @Override
    public Produto buscar(int id) {
        String sql = "SELECT * FROM produtos WHERE id = ?";

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)
        ) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return mapearProduto(resultSet);
            }

            return null;

        } catch (SQLException e) {
        logger.error("Erro ao buscar produto (em transação) id = {}",id ,e);
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
                        dataSource.getConnection();

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
        logger.error("Erro ao buscar produto por nome: {}",nomeBusca, e);
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
            logger.error("Erro ao atualizar quantidade produto id = {}, nova quantidade = {}", id, novaQuantidade, e);
            throw new PersistenciaException("Erro ao atualizar quantidade.",e);
        }
    }

    @Override
    public List<Produto> buscarEstoqueBaixo() {
        String sql = "SELECT * FROM produtos " +
                "WHERE quantidade <= 5 ";

        try (
                Connection connection =
                        dataSource.getConnection();

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
            logger.error("Erro ao buscar estoque baixo.", e);
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
                        dataSource.getConnection();

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
            logger.error("Erro ao buscar produtos ativos.", e);
            throw new PersistenciaException("Erro ao buscar produtos ativos.",e);
        }
    }

    @Override
    public double calcularValorTotalEstoque() {
        String sql = "SELECT SUM(preco * quantidade) AS total " +
                "FROM produtos ";

        try (
                Connection connection =
                        dataSource.getConnection();

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
            logger.error("Erro ao calcular valor total do estoque.", e);
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
        produto.setDesativadoManualmente(rs.getBoolean("desativado_manualmente"));

        return produto;
    }

    @Override
    public int contaProdutos() {
        String sql =
                " SELECT COUNT(*) AS total " +
                        " FROM produtos ";

        try (
                Connection connection =
                        dataSource.getConnection();

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
            logger.error("Erro ao contar produto.", e);
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
                        dataSource.getConnection();

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
            logger.error("Erro ao contar produtos ativos.", e);
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
                        dataSource.getConnection();

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
            logger.error("Erro ao contar produtos inativos.", e);
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
                        dataSource.getConnection();

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
            logger.error("Erro ao contar quantidade total de produtos.", e);
            throw new PersistenciaException("Erro ao contar o total de produtos.",e);
        }
    }

    @Override
    public List<ProdutoRanking> buscarRankingProdutos() {
        String sql =
                "SELECT * FROM  vw_ranking_produtos";

        try (
                Connection connection =
                        dataSource.getConnection();

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
            logger.error("Erro ao buscar ranking de produtos.", e);
            throw new PersistenciaException("Erro ao buscar ranking de produtos.",e);
        }
    }

    @Override
    public ResumoEstoque buscarResumoEstoque() {

        String sql = "{CALL sp_resumo_estoque()}";

        try (
                Connection connection = dataSource.getConnection();
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
            logger.error("Erro ao buscar resumo do estoque.", e);
            throw new PersistenciaException("Erro ao resumir estoque.",e);
        }
    }

    @Override
    public double calcularValorProduto(int id) {
        String sql = "{ ? =  call fn_calcular_valor_produto(?) }";

        try (
                Connection connection = dataSource.getConnection();

                CallableStatement statement = connection.prepareCall(sql);

                )
        {
            statement.registerOutParameter(1, Types.DECIMAL);

            statement.setInt(2, id);

            statement.execute();

            return statement.getDouble(1);

        }catch (SQLException e){
            logger.error("Erro ao calculara valor do produto id = {}.", id , e);
            throw new PersistenciaException("Erro ao calcular valor do produto.",e);
        }
    }

    @Override
    public void inserirProdutoEmLote(List<Produto> produtos) {
        String sql = """
                    INSERT INTO produtos (nome, preco, quantidade, status, desativado_manualmente)
                    VALUES (?, ?, ?, ?, ?)
                    """;

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = dataSource.getConnection();
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
                    logger.error("Erro ao exevutar rollback do lote.", ex);
                    throw new PersistenciaException("Erro ao executar rollback do lote.",ex);
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
                logger.error("Erro ao fechar conexão após inserir produtos em lote.", e);
                throw new PersistenciaException("Erro ao inserir produto em lote.",e);
            }
        }
    }

    @Override
    public void inserirProdutosComSavepoint(List<Produto> produtos) {

        String sql = """
                insert into produtos(nome, preco, quantidade, status, desativado_manualmente)
                values (?, ?, ?, ?, ?)
                """;

        Connection connection= null;
        PreparedStatement statement = null;
        Savepoint savepoint = null;
        try {

            connection = dataSource.getConnection();
            connection.setAutoCommit(false);

            logger.info("Iniciando transação de inserção com savepoint. Total de produtos: {}", produtos.size());
            statement = connection.prepareStatement(sql);

            int contador = 0;

            for (Produto produto : produtos) {

                preencherStatement(statement,produto);

                logger.info("Inserindo produto: {}", produto.getNome());
                statement.executeUpdate();
                contador++;

                if (contador == 2){
                    savepoint = connection.setSavepoint("PONTO_SEGURO");
                }

                if (contador == 4){
                    throw new SQLException("Erro simulado para teste");
                }

            }

            connection.commit();
            logger.info("Todos os produtos foram cadastrados com sucesso. Total: {}", produtos.size());

        }catch (SQLException e){
            logger.error("Erro ao inserir produtos com savepoint.", e);
            try {
                if (connection !=  null && savepoint != null){
                    connection.rollback(savepoint);
                    logger.info("Rollback realizado com savepoint.");
                    connection.commit();
                    logger.info("Commit realizado após rollback parcial.");
                }else {
                    connection.rollback();
                    logger.info("Rollback total realizado.");
                }
            }catch (SQLException e1){
                logger.error("Erro ao executar rollback com savepoint.", e1);
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
                logger.error("Erro ao fechar conexão após inserir produtos com savepoint.", e);
                throw new PersistenciaException("Erro ao inserir produto com savePoint.",e);
            }
        }
    }

    @Override
    public void reativar(int id) {
        String sql = """
                UPDATE produtos
                SET status = 'ATIVO', desativado_manualmente = FALSE 
                WHERE id = ?
                """;
        try (
                Connection connection =
                        dataSource.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new PersistenciaException("Erro ao reativar produto.",e);
        }
    }

    @Override
    public void atualizarStatus(Connection connection, int id, StatusProduto status) {
        String sql = """
                UPDATE produtos 
                SET status = ? 
                WHERE id = ?
                """;

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setString(1, status.name());

            statement.setInt(2, id);

            statement.executeUpdate();

        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao atualizar status.",e);
        }
    }

    @Override
    public void gerarSnapshotDashboard() {
        String sql = "{call sp_snapshot_dashboard()}";

        try (
                Connection connection = dataSource.getConnection();
                CallableStatement statement = connection.prepareCall(sql);

                ){
            statement.execute();
            logger.info("Snapshot do dashboard gerado com sucesso.");

        }catch (SQLException e){
            logger.error("Erro ao gerar snapshot do dashboard.", e);
            throw new PersistenciaException("Erro ao gerar snapshot do dashboard.", e);
        }
    }

    private void preencherStatement(PreparedStatement statement, Produto produto) throws SQLException {

        statement.setString(1, produto.getNome());
        statement.setDouble(2,produto.getPreco());
        statement.setInt(3,produto.getQuantidade());
        statement.setString(4,produto.getStatus().name());
        statement.setBoolean(5, produto.isDesativadoManualmente());

    }
}