package dao;
import db.ConnectionFactory;
import model.Movimentacao;
import model.TipoMovimentacao;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO implements MovimentacaoRepository{

    @Override
    public void registrarMovimentacao(Connection connection, Movimentacao movimentacao){

        String sql =
                " INSERT INTO movimentacoes "+
                "(produto_id, tipo, quantidade)"+
                "VALUES (?, ?, ?)";

        try (
                PreparedStatement statement =
                        connection.prepareStatement(sql)

                )
        {

            statement.setInt(1,movimentacao.getProduto_id());

            statement.setString(2, movimentacao.getTipo().name());

            statement.setInt(3, movimentacao.getQuantidade());

            statement.executeUpdate();

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    private Movimentacao mapearMovimentacao(ResultSet rs) throws SQLException{
        Movimentacao movimentacao =new Movimentacao();

        movimentacao.setId(rs.getInt("id"));
        movimentacao.setProduto_id(rs.getInt("produto_id"));
        movimentacao.setTipo(TipoMovimentacao.valueOf(rs.getString("tipo")));
        movimentacao.setQuantidade(rs.getInt("quantidade"));
        movimentacao.setDataMovimentacao(rs.getDate("data_movimentacao").toLocalDate());
        movimentacao.setNomeProduto(rs.getString("nome"));

        return movimentacao;
    }

    @Override
    public List<Movimentacao> listar(){
        String sql =
                "SELECT m.id," +
                        "m.produto_id,"+
                        "p.nome, " +
                        "m.tipo, " +
                        "m.quantidade, " +
                        "m.data_movimentacao "+
                "FROM movimentacoes m " +
                        "INNER JOIN produtos p " +
                        "on m.produto_id = p.id ";

        try (
                Connection connection = ConnectionFactory.getConnection();

                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery()
        )

        {List<Movimentacao> movimentacoes = new ArrayList<>();

            while (resultSet.next()){
                movimentacoes.add(mapearMovimentacao(resultSet));

            }

            return movimentacoes;

        }catch (SQLException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Movimentacao> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT " +
                "m.id, " +
                "m.produto_id, " +
                "p.nome, " +
                "m.tipo," +
                "m.quantidade, " +
                "m.data_movimentacao " +
                "FROM movimentacoes m " +
                "INNER JOIN produtos p " +
                "on m.produto_id = p.id " +
                "WHERE m.data_movimentacao " +
                "BETWEEN ? AND ? ";

        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setDate(1, Date.valueOf(dataInicio));
            statement.setDate(2, Date.valueOf(dataFim));

            try (ResultSet resultSet = statement.executeQuery()){

                List<Movimentacao> listaPorDatas = new ArrayList<>();

                while (resultSet.next()){
                    listaPorDatas.add(mapearMovimentacao(resultSet));
                }

                return listaPorDatas;
            }

        }catch (SQLException e){

            throw new RuntimeException(e);
        }
        }
}
