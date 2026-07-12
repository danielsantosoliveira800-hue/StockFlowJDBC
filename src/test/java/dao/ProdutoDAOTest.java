package dao;

import integration.IntegrationTestBase;
import model.Produto;
import model.StatusProduto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoDAOTest extends IntegrationTestBase {

    private final ProdutoDAO produtoDAO = new ProdutoDAO();

    @Test
    void deveSalvarProdutoNoBanco(){
        Produto produto = new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO);

        produtoDAO.salvarProduto(produto);
        Produto produto1 = produtoDAO.buscar(1);

        assertNotNull(produto1);
        assertEquals("Monitor", produto1.getNome());
        assertEquals(900.0, produto1.getPreco());
        assertEquals(10, produto1.getQuantidade());
    }

    @Test
    void deveRetornarNuloQuandoProdutoNaoExistir(){
        Produto produto = produtoDAO.buscar(999);

        assertNull(produto);
    }

    @Test
    void deveListarTodosOsProdutos(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO));

        List<Produto> produtos = produtoDAO.listar();

        assertEquals(2, produtos.size());
    }

    @Test
    void deveAtualizarPrecoDoProduto(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        produtoDAO.atualizar(1, 750.0);

        Produto produtoAtualizado = produtoDAO.buscar(1);

        assertEquals("Monitor", produtoAtualizado.getNome());
        assertEquals(750.0, produtoAtualizado.getPreco());

    }

    @Test
    void deveDesativarProduto(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));

        produtoDAO.desativar(1);

        Produto produtoDesativado = produtoDAO.buscar(1);
        assertEquals(StatusProduto.INATIVO, produtoDesativado.getStatus());
        assertTrue(produtoDesativado.isDesativadoManualmente());
    }
}