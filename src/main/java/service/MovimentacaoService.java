package service;

import dao.MovimentacaoDAO;
import dao.MovimentacaoRepository;
import dao.ProdutoDAO;
import dao.ProdutoRepository;
import db.ConnectionFactory;
import exception.TipoMovimentacaoInvalidaException;
import model.Movimentacao;
import model.Produto;
import model.TipoMovimentacao;
import validation.MovimentacaoValidation;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MovimentacaoService {

    private static final TipoMovimentacao ENTRADA = TipoMovimentacao.ENTRADA;
    private static final TipoMovimentacao SAIDA = TipoMovimentacao.SAIDA;

    private final MovimentacaoRepository movimentacaoRepository;

    private final ProdutoRepository produtoRepository;

    private final MovimentacaoValidation movimentacaoValidation;

    public MovimentacaoService() {

        this(new MovimentacaoDAO(),new ProdutoDAO(), new MovimentacaoValidation());
    }

    public MovimentacaoService(MovimentacaoRepository movimentacaoRepository,
                               ProdutoRepository produtoRepository,
                               MovimentacaoValidation movimentacaoValidation) {

        this.movimentacaoRepository = movimentacaoRepository;
        this.produtoRepository = produtoRepository;
        this.movimentacaoValidation = movimentacaoValidation;
    }

    public void registrarMovimentacao(Movimentacao movimentacao) {

        movimentacaoValidation.validarMovimentacao(movimentacao);

            Connection connection = null;
            try {

                connection = ConnectionFactory.getConnection();
                connection.setAutoCommit(false);

                Produto produto = buscarProduto(connection, movimentacao.getProduto_id());

                int novaQuantidade = calcularNovaQuantidade(produto,movimentacao);

                produtoRepository.atualizarQuantidade(connection, produto.getId(), novaQuantidade);

                movimentacaoRepository.registrarMovimentacao(connection, movimentacao);

                commit(connection);

            } catch (Exception e) {

                rollback(connection);
                throw new RuntimeException(e);

            } finally {

                fecharConexao(connection);
            }
        }

    private void commit(Connection connection){
        try {

            if (connection != null){
                connection.commit();
            }
        }catch (SQLException e){

            throw new RuntimeException(e);

        }
    }

    private Produto buscarProduto(Connection connection, int id){

        Produto produto = produtoRepository.buscar(connection, id);

        movimentacaoValidation.validarProduto(produto);

        return produto;
    }

    private int calcularNovaQuantidade(Produto produto, Movimentacao movimentacao){
        int quantidadeAtual = produto.getQuantidade();

        if (movimentacao.getTipo() == TipoMovimentacao.ENTRADA){
            return quantidadeAtual+ movimentacao.getQuantidade();
        }

        if (movimentacao.getTipo() == TipoMovimentacao.SAIDA){
            movimentacaoValidation.validarSaida(produto, movimentacao);

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
            throw new RuntimeException("Erro ao executar o rollback.");
        }
    }

    private void fecharConexao(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao fechar o banco.");
        }
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoRepository.listar();
    }

    public List<Movimentacao> buscarMovimentacoesPorData(LocalDate dataInicio, LocalDate dataFim) {
        return movimentacaoRepository.buscarPorPeriodo(dataInicio, dataFim);
    }
}

