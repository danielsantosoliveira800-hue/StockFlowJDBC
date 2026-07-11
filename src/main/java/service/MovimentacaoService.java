package service;

import dao.MovimentacaoDAO;
import dao.MovimentacaoRepository;
import dao.ProdutoDAO;
import dao.ProdutoRepository;
import db.ConnectionFactory;
import exception.PersistenciaException;
import exception.TipoMovimentacaoInvalidaException;
import model.Movimentacao;
import model.Produto;
import model.StatusProduto;
import model.TipoMovimentacao;
import validation.MovimentacaoValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MovimentacaoService {

    private static final TipoMovimentacao ENTRADA = TipoMovimentacao.ENTRADA;
    private static final TipoMovimentacao SAIDA = TipoMovimentacao.SAIDA;
    private final MovimentacaoRepository movimentacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final MovimentacaoValidator movimentacaoValidator;
    private final DataSource dataSource;

    public MovimentacaoService() {

        this(new MovimentacaoDAO(),new ProdutoDAO(), new MovimentacaoValidator(), ConnectionFactory.getDataSource());
    }

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
                               ProdutoRepository produtoRepository,
                               MovimentacaoValidator movimentacaoValidator,
                               DataSource dataSource) {

        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoValidator = movimentacaoValidator;
        this.dataSource = dataSource;
    }

    public void registrarMovimentacao(Movimentacao movimentacao) {

        movimentacaoValidator.validarMovimentacao(movimentacao);

            Connection connection = null;
            try {

                connection = dataSource.getConnection();
                connection.setAutoCommit(false);

                Produto produto = buscarProduto(connection, movimentacao.getProduto_id());

                int novaQuantidade = calcularNovaQuantidade(produto,movimentacao);

                produtoRepository.atualizarQuantidade(connection, produto.getId(), novaQuantidade);

                if (!produto.isDesativadoManualmente()){
                    StatusProduto novoStatus =
                            novaQuantidade == 0 ? StatusProduto.INATIVO : StatusProduto.ATIVO;

                    produtoRepository.atualizarStatus(connection, produto.getId(), novoStatus);
                }

                movimentacaoRepository.registrarMovimentacao(connection, movimentacao);

                commit(connection);

            } catch (RuntimeException e) {

                rollback(connection);
                throw e;

            }catch (SQLException e){
                rollback(connection);
                throw new PersistenciaException("Erro ao registrar movimentação.",e);
            }finally {

                fecharConexao(connection);
            }
        }

    private void commit(Connection connection){
        try {

            if (connection != null){
                connection.commit();
            }
        }catch (SQLException e){

            throw new PersistenciaException("Erro ao confirmar a transação.",e);

        }
    }

    private Produto buscarProduto(Connection connection, int id){

        Produto produto = produtoRepository.buscar(connection, id);

        movimentacaoValidator.validarProduto(produto);

        return produto;
    }

    private int calcularNovaQuantidade(Produto produto, Movimentacao movimentacao){
        int quantidadeAtual = produto.getQuantidade();

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA){
            return quantidadeAtual+ movimentacao.getQuantidade();
        }

        if (movimentacao.getTipo() == TipoMovimentacao.SAIDA){
            movimentacaoValidator.validarSaida(produto, movimentacao);

            return quantidadeAtual - movimentacao.getQuantidade();
        }

        throw new TipoMovimentacaoInvalidaException();
    }

    private void rollback(Connection connection) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao executar o rollback.",e);
        }
    }

    private void fecharConexao(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao fechar a conexão com o banco de dados.",e);
        }
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.listar();
    }

    public List<Movimentacao> buscarMovimentacoesPorData(LocalDate dataInicio, LocalDate dataFim) {
        return movimentacaoRepository.buscarPorPeriodo(dataInicio, dataFim);
    }
}

