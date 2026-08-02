package infrasctructure.persistence;

import dao.AuditoriaProdutosRepository;
import exception.PersistenciaException;
import domain.model.AuditoriaProdutos;
import infrasctructure.ConnectionFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AuditoriaProdutosDAO implements AuditoriaProdutosRepository {

    private final DataSource dataSource;

    public AuditoriaProdutosDAO() {
        this(ConnectionFactory.getDataSource());
    }

    public AuditoriaProdutosDAO(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<AuditoriaProdutos> listar() {

        String sql =
                "SELECT * " +
                "FROM auditoria_produtos " +
                "ORDER BY data_alteracao DESC";

        try (
                Connection connection =
                        dataSource.getConnection();

                PreparedStatement statement
                        = connection.prepareStatement(sql);

                ResultSet resultSet = statement.executeQuery();
                ){

            List<AuditoriaProdutos> auditorias = new ArrayList<>();

            while (resultSet.next()){

                auditorias.add(mapearAuditoria(resultSet));

            }

            return auditorias;
        }catch (SQLException e){
            throw new PersistenciaException("Erro ao listar auditorias.",e);
        }
    }

    private AuditoriaProdutos mapearAuditoria(ResultSet rs) throws SQLException{

        AuditoriaProdutos auditoriaProdutos = new AuditoriaProdutos();

        auditoriaProdutos.setId(rs.getInt("id"));
        auditoriaProdutos.setProdutoId(rs.getInt("produto_id"));
        auditoriaProdutos.setOperacao(rs.getString("operacao"));

        auditoriaProdutos.setNomeAntigo(rs.getString("nome_antigo"));
        auditoriaProdutos.setNomeNovo(rs.getString("nome_novo"));

        auditoriaProdutos.setPrecoAntigo(rs.getDouble("preco_antigo"));
        auditoriaProdutos.setPrecoNovo(rs.getDouble("preco_novo"));

        auditoriaProdutos.setQuantidadeAntiga(rs.getInt("quantidade_antiga"));
        auditoriaProdutos.setQuantidadeNova(rs.getInt("quantidade_nova"));

        auditoriaProdutos.setStatusAntigo(rs.getString("status_antigo"));
        auditoriaProdutos.setStatusNovo(rs.getString("status_novo"));

        auditoriaProdutos.setUsuarioBanco(rs.getString("usuario_banco"));

        auditoriaProdutos.setDataAlteracao(rs.getTimestamp("data_alteracao").toLocalDateTime());

        return auditoriaProdutos;
    }
}
