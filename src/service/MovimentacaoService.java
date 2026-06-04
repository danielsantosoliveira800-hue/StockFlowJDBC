package service;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.Movimentacao;
import model.Produto;

import java.time.LocalDate;
import java.util.List;

public class MovimentacaoService {

    private MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();

    private ProdutoDAO produtoDAO = new ProdutoDAO();

    public void registrarMovimentacao(  Movimentacao movimentacao){

        Produto produto = produtoDAO.buscar(movimentacao.getProduto_id());

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado. ");
        }

        int quantidadeAtual = produto.getQuantidade();

        int novaQuantidade;

        if (movimentacao.getTipo().equals("ENTRADA")){
            novaQuantidade = quantidadeAtual + movimentacao.getQuantidade();

        } else if (movimentacao.getTipo().equals("SAIDA")) {

            if (movimentacao.getQuantidade() > quantidadeAtual){
                throw new IllegalArgumentException("Quantidade insuficiente no estoque.");
            }

            novaQuantidade = quantidadeAtual - movimentacao.getQuantidade();

        }else {
            throw new IllegalArgumentException("Tipo de movimentação invalida.");
        }

        produtoDAO.atualizarQuantidade(produto.getId(), novaQuantidade);

        movimentacaoDAO.registrarMovimentacao(movimentacao);

    }

    public List<Movimentacao> listarMovimentacoes (){
        return movimentacaoDAO.listar();
    }

    public List<Movimentacao> buscarMovimentacoesPorData(LocalDate dataInicio, LocalDate dataFim){
       return movimentacaoDAO.buscarPorPeriodo(dataInicio, dataFim);
    }

}
