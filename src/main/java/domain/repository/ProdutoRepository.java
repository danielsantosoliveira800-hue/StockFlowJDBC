package domain.repository;

import domain.model.Produto;
import java.util.List;

public interface ProdutoRepository {
    void salvarProduto(Produto produto);
    Produto buscar(int id);
    List<Produto> listar();
    List<Produto> buscarPorNome(String nomeBusca);
    void atualizar(int id, double novoPreco);
    void desativar(int id);
    void reativar(int id);
}