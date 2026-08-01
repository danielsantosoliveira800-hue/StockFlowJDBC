package domain.repository;

import domain.model.Produto;
import java.util.List;

public interface ProdutoLoteRepository {
    void inserirProdutoEmLote(List<Produto> produtos);
    void inserirProdutosComSavepoint(List<Produto> produtos);
}