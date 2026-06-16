package dao;

import db.ConnectionFactory;
import model.Produto;
import model.ProdutoRanking;
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deletar(int id) {

        String sql = "DELETE FROM  produtos " +
                "WHERE id = ? ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);

            statement.executeUpdate();

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    @Override
    public Produto buscar(Connection connection ,int id){
        String sql =
                "SELECT * FROM produtos " +
                        "WHERE id = ? ";

        try (
                PreparedStatement statement =
                    connection.prepareStatement(sql);)
        {
            statement.setInt(1, id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()){
                return mapearProduto(resultSet);

            }

            return null;
        }catch (SQLException e){
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
            throw new RuntimeException(e);
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
    public int contaProdutos(){
        String sql =
                " SELECT COUNT(*) AS total "+
                " FROM produtos ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
                )

        {if (resultSet.next()){
            return resultSet.getInt("total");
        }

        return 0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int contaProdutosAtivos(){
        String sql =
                "SELECT COUNT(*) AS total "+
                "FROM produtos "+
                "WHERE status = 'ATIVO' ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();
                )
        {
            if (resultSet.next()){
            return resultSet.getInt("total");
        }
            return 0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int contaProdutosInativos(){
        String sql =
                "SELECT COUNT(*) AS total "+
                "FROM produtos "+
                "WHERE status = 'INATIVO'";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();

                )
        {
            if (resultSet.next()){
            return resultSet.getInt("total");

        }
        return 0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public int quantidadeTotalProdutos(){
        String sql =
                "SELECT SUM(quantidade) AS total "+
                "FROM produtos ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery();

                )
        {
            if (resultSet.next()){
            return resultSet.getInt("total");
        }
        return  0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<ProdutoRanking> buscarRankingProdutos(){
        String sql =
                "SELECT * FROM  vw_ranking_produtos";

        try (
                Connection connection =
                         ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql);

                ResultSet resultSet =
                        statement.executeQuery())
        {
            List<ProdutoRanking> rankingDeProdutos = new ArrayList<>();

            while (resultSet.next()){
                ProdutoRanking produtoRanking = new ProdutoRanking();

                produtoRanking.setNomeProduto(resultSet.getString("nome"));
                produtoRanking.setQuantidadeMovimentada(resultSet.getInt("quantidade_movimentada"));
                produtoRanking.setTotalMovimentacoes(resultSet.getInt("total_movimentacoes"));

                rankingDeProdutos.add(produtoRanking);
            }
            return rankingDeProdutos;
        }catch (SQLException e){

            throw new RuntimeException(e);
        }
    }
}