package domain.repository;

import domain.model.Produto;
import domain.model.ProdutoRanking;
import domain.model.ResumoEstoque;
import java.util.List;

public interface ProdutoConsultaRepository {
    int contaProdutos();
    int contaProdutosAtivos();
    int contaProdutosInativos();
    int quantidadeTotalProdutos();
    double calcularValorTotalEstoque();
    double calcularValorProduto(int id);
    List<Produto> buscarEstoqueBaixo();
    List<Produto> buscarProdutosAtivos();
    List<ProdutoRanking> buscarRankingProdutos();
    ResumoEstoque buscarResumoEstoque();
    void gerarSnapshotDashboard();
}