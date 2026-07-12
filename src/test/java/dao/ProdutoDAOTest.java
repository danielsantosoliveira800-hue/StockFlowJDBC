package dao;

import integration.IntegrationTestBase;
import model.Produto;
import model.ResumoEstoque;
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

    @Test
    void deveBuscarProdutoPorNome(){
        produtoDAO.salvarProduto(new Produto("Notebook Dell", 3500.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Notebook Lenovo", 2800.0, 3, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.ATIVO));

        List<Produto> produtos = produtoDAO.buscarPorNome("Notebook");

        assertEquals(2, produtos.size());
    }

    @Test
    void deveBuscarProdutosComEstoqueBaixo(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 3, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO));

        List<Produto> resultado = produtoDAO.buscarEstoqueBaixo();

        assertEquals(1, resultado.size());
        assertEquals("Monitor", resultado.get(0).getNome());
    }

    @Test
    void deveCalcularValorTotalDoEstoque(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));

        double total = produtoDAO.calcularValorTotalEstoque();

        assertEquals(9500.0, total);
    }

    @Test
    void deveContarTotalDeProdutos(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.INATIVO));

        int total = produtoDAO.contaProdutos();

        assertEquals(3, total);
    }

    @Test
    void deveContarProdutosAtivos(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.INATIVO));

        int total = produtoDAO.contaProdutosAtivos();

        assertEquals(2, total);
    }

    @Test
    void deveContarProdutosInativos(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.INATIVO));

        int total = produtoDAO.contaProdutosInativos();

        assertEquals(1, total);
    }

    @Test
    void deveSomarQuantidadeTotalDeProdutos(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.INATIVO));

        int total = produtoDAO.quantidadeTotalProdutos();

        assertEquals(35, total);
    }

    @Test
    void deveBuscarResumoDoEstoque(){
        produtoDAO.salvarProduto(new Produto("Monitor", 900.0, 10, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Teclado", 100.0, 5, StatusProduto.ATIVO));
        produtoDAO.salvarProduto(new Produto("Mouse", 40.0, 20, StatusProduto.INATIVO));

        ResumoEstoque resumo = produtoDAO.buscarResumoEstoque();

        assertEquals(3, resumo.getTotalProdutos());
        assertEquals(35, resumo.getQuantidadeTotalProdutos());
        assertEquals(10300.0, resumo.getValorTotalEstoque());
        assertEquals(2, resumo.getProdutosAtivos());
        assertEquals(1, resumo.getProdutosInativos());
    }
}