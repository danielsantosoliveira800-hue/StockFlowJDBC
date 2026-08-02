package infrasctructure.persistence;
import domain.repository.MovimentacaoRepository;
import exception.PersistenciaException;
import domain.model.Movimentacao;
import domain.model.TipoMovimentacao;
import infrasctructure.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoDAO implements MovimentacaoRepository {

    private static final Logger logger = LoggerFactory.getLogger(MovimentacaoDAO.class);
    private final DataSource dataSource;

    public MovimentacaoDAO(){
        this(ConnectionFactory.getDataSource());
    }

    public MovimentacaoDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
            logger.error("Erro ao registrar movimentação: produto_id={}, tipo={}, quantidade={}",
                    movimentacao.getProduto_id(), movimentacao.getTipo(), movimentacao.getQuantidade(), e);
            throw new PersistenciaException("Erro ao registrar movimentação.",e);
        }
    }

    private Movimentacao mapearMovimentacao(ResultSet rs) throws SQLException{
        Movimentacao movimentacao =new Movimentacao();

        movimentacao.setId(rs.getInt("id"));
        movimentacao.setProduto_id(rs.getInt("produto_id"));
        movimentacao.setTipo(TipoMovimentacao.valueOf(rs.getString("tipo")));
        movimentacao.setQuantidade(rs.getInt("quantidade"));
        movimentacao.setDataMovimentacao(rs.getDate("data_movimentacao").toLocalDate());
        movimentacao.setNomeProduto(rs.getString("nome_produto"));

        return movimentacao;
    }

    @Override
    public List<Movimentacao> listar(){
        String sql =
                "SELECT * FROM vw_historico_movimentacoes";

        try (
                Connection connection = dataSource.getConnection();

                PreparedStatement statement = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery()
        )

        {List<Movimentacao> movimentacoes = new ArrayList<>();

            while (resultSet.next()){
                movimentacoes.add(mapearMovimentacao(resultSet));

            }

            return movimentacoes;

        }catch (SQLException e){

            logger.error("Erro ao listar movimentações.", e);
            throw new PersistenciaException("Erro ao listar movimentações.",e);
        }
    }

    @Override
    public List<Movimentacao> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim) {
        String sql = "SELECT " +
                "m.id, " +
                "m.produto_id, " +
                "p.nome AS nome_produto, " +
                "m.tipo," +
                "m.quantidade, " +
                "m.data_movimentacao " +
                "FROM movimentacoes m " +
                "INNER JOIN produtos p " +
                "on m.produto_id = p.id " +
                "WHERE m.data_movimentacao >= ? AND m.data_movimentacao < ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql))
        {
            statement.setDate(1, Date.valueOf(dataInicio));
            statement.setDate(2, Date.valueOf(dataFim.plusDays(1)));

            try (ResultSet resultSet = statement.executeQuery()){

                List<Movimentacao> Movimentacoes = new ArrayList<>();

                while (resultSet.next()){
                    Movimentacoes.add(mapearMovimentacao(resultSet));
                }

                return Movimentacoes;
            }

        }catch (SQLException e){

            logger.error("Erro ao buscar movimentação por período: {} até {}", dataInicio, dataFim, e);
            throw new PersistenciaException("Erro ao buscar movimentação por periodo.",e);
        }
        }
}
