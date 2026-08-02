package service;

import domain.model.Produto;
import domain.model.ProdutoRanking;
import domain.model.ResumoEstoque;
import domain.repository.ProdutoConsultaRepository;
import domain.repository.ProdutoRepository;
import exception.ProdutoNaoEncontradoException;
import exception.ValidacaoException;

import java.util.List;

public class ProdutoRelatorioService {

    private final ProdutoConsultaRepository produtoConsultaRepository;
    private final ProdutoRepository produtoRepository;

    public ProdutoRelatorioService(ProdutoConsultaRepository produtoConsultaRepository,
                                   ProdutoRepository produtoRepository) {
        this.produtoConsultaRepository = produtoConsultaRepository;
        this.produtoRepository = produtoRepository;
    }

    public List<Produto> buscarEstoqueBaixo(){
        return produtoConsultaRepository.buscarEstoqueBaixo();
    }

    public List<Produto> buscarEstoqueAtivo(){
        return produtoConsultaRepository.buscarProdutosAtivos();
    }

    public double calcularValorTotalEstoque(){
        return produtoConsultaRepository.calcularValorTotalEstoque();
    }

    public int contarProdutos(){
        return produtoConsultaRepository.contaProdutos();
    }

    public int contaProdutosAtivos(){
        return produtoConsultaRepository.contaProdutosAtivos();
    }

    public int contaProdutosInativos(){
        return produtoConsultaRepository.contaProdutosInativos();
    }

    public int somaQuantidadeProdutos(){
        return produtoConsultaRepository.quantidadeTotalProdutos();
    }

    public List<ProdutoRanking> buscarProdutoRanking(){
        return produtoConsultaRepository.buscarRankingProdutos();
    }

    public ResumoEstoque buscarResumoEstoque(){
        return produtoConsultaRepository.buscarResumoEstoque();
    }

    public double calcularValorProduto(int id){
        validarExistencia(id);
        return produtoConsultaRepository.calcularValorProduto(id);
    }

    private void validarExistencia(int id){
        if (id <= 0){
            throw new ValidacaoException("ID inválido.");
        }

        Produto produto = produtoRepository.buscar(id);

        if (produto == null){
            throw new ProdutoNaoEncontradoException();
        }
    }
}