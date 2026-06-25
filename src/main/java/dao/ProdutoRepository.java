package dao;

import model.Produto;
import model.ProdutoRanking;
import model.ResumoEstoque;

import java.sql.Connection;
import java.util.List;

public interface ProdutoRepository {

    void salvarProduto(Produto produto);

    Produto buscar(Connection connection, int id);

    Produto buscar(int id);

    List<Produto> listar();

    void atualizar(int id, double novoPreco);

    void desativar(int id);

    List<Produto> buscarPorNome(String nome);

    void atualizarQuantidade(Connection connection, int id, int novaQuantidade);

    List<Produto> buscarEstoqueBaixo();

    List<Produto> buscarProdutosAtivos();

    double calcularValorTotalEstoque();

    int contaProdutos();

    int contaProdutosAtivos();

    int contaProdutosInativos();

    int quantidadeTotalProdutos();

    List<ProdutoRanking> buscarRankingProdutos();

    ResumoEstoque buscarResumoEstoque();

    double calcularValorProduto(int id);
}