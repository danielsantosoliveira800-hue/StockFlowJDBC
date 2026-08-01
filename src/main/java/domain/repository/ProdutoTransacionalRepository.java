package domain.repository;

import domain.model.Produto;
import domain.model.StatusProduto;
import java.sql.Connection;

public interface ProdutoTransacionalRepository {
    Produto buscar(Connection connection, int id);
    void atualizarQuantidade(Connection connection, int id, int novaQuantidade);
    void atualizarStatus(Connection connection, int id, StatusProduto status);
}