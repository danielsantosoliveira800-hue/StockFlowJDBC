package dao;

import db.ConnectionFactory;
import model.Produto;

import javax.xml.transform.Result;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {

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
            statement.setString(4, produto.getStatus());

            statement.executeUpdate();

            System.out.println("Produto salvo com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

            System.out.println("Preço atualizado com sucesso.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

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

            System.out.println("Produto excluido com sucesso.");

        } catch (SQLException e) {

            throw new RuntimeException(e);
        }
    }

    public Produto buscar(int id) {
        String sql =
                "SELECT * FROM produtos " +
                        "WHERE id = ?";
        try (
                Connection connection =
                        ConnectionFactory.getConnection();

                PreparedStatement statement =
                        connection.prepareStatement(sql)) {
            statement.setInt(1, id);

            ResultSet resultSet =
                    statement.executeQuery();

            if (resultSet.next()) {

                return mapearProduto(resultSet);

            }

            return null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Produto> buscarPorNome(String nomeBusca) {

        String sql =
                "SELECT * FROM produtos" +
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

                Produto produto = new Produto();

                produto.setId(resultSet.getInt("id"));
                produto.setNome(resultSet.getString("nome"));
                produto.setPreco(resultSet.getDouble("preco"));
                produto.setQuantidade(resultSet.getInt("quantidade"));
                produto.setStatus(resultSet.getString("status"));

                produtos.add(produto);
            }

            return produtos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void atualizarQuantidade(int id, int novaQuantidade) {
        String sql =
                "UPDATE produtos " +
                        "SET quantidade  = ? " +
                        "WHERE id = ? ";

        try (
                Connection connection =
                        ConnectionFactory.getConnection();

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
            throw new RuntimeException(e.getMessage());
        }
    }

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
        produto.setStatus(rs.getString("status"));


        return produto;
    }

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
        {if (resultSet.next()){
            return resultSet.getInt("total");
        }
            return 0;

        }catch (SQLException e){
            throw new RuntimeException();
        }
    }

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
        {if (resultSet.next()){
            return resultSet.getInt("total");

        }
        return 0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    public int qunatidadeTotalProdutos(){
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
        {if (resultSet.next()){
            return resultSet.getInt("total");
        }
        return  0;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }
}