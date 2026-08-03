package service;

import domain.model.*;
import domain.repository.ProdutoConsultaRepository;
import domain.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ProdutoRelatorioServiceTest {

    @Mock
    private ProdutoConsultaRepository produtoConsultaRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    private ProdutoRelatorioService produtoRelatorioService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoRelatorioService = new ProdutoRelatorioService(produtoConsultaRepository, produtoRepository);
    }

    @Test
    @DisplayName("Deve retornar o resumo do estoque.")
    void deveRetornarResumoDoEstoque() {
        ResumoEstoque resumoMock = new ResumoEstoque();
        resumoMock.setProdutosInativos(30);
        resumoMock.setProdutosAtivos(970);
        resumoMock.setTotalProdutos(1000);
        resumoMock.setQuantidadeTotalProdutos(3056);
        resumoMock.setValorTotalEstoque(860996.00);

        when(produtoConsultaRepository.buscarResumoEstoque()).thenReturn(resumoMock);

        ResumoEstoque resultado = produtoRelatorioService.buscarResumoEstoque();

        assertEquals(30, resultado.getProdutosInativos());
        assertEquals(970, resultado.getProdutosAtivos());
        verify(produtoConsultaRepository).buscarResumoEstoque();
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos com estoque baixo. ")
    void retornaListaDeProdutosComEstoeueBaixo() {
        List<Produto> estoqueBaixoMock = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoConsultaRepository.buscarEstoqueBaixo()).thenReturn(estoqueBaixoMock);

        List<Produto> resultado = produtoRelatorioService.buscarEstoqueBaixo();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoConsultaRepository).buscarEstoqueBaixo();
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos com estoque ativo.")
    void retornaListaDeProdutosAtivos() {
        List<Produto> produtosAtivos = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoConsultaRepository.buscarProdutosAtivos()).thenReturn(produtosAtivos);

        List<Produto> resultado = produtoRelatorioService.buscarEstoqueAtivo();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoConsultaRepository).buscarProdutosAtivos();
    }

    @Test
    @DisplayName("Deve retoenar um ranking de produtos.")
    void retornaRankingdeProdutos() {
        List<ProdutoRanking> rankingMock = List.of(
                new ProdutoRanking("Teclado", 12, 340)
        );

        when(produtoConsultaRepository.buscarRankingProdutos()).thenReturn(rankingMock);

        List<ProdutoRanking> resultado = produtoRelatorioService.buscarProdutoRanking();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNomeProduto());
        verify(produtoConsultaRepository).buscarRankingProdutos();
    }

    @Test
    @DisplayName("deve retornar a quantidade de produtos.")
    void deveRetornarAQuantidadeDeProdutos() {
        when(produtoConsultaRepository.contaProdutos()).thenReturn(10);

        int total = produtoRelatorioService.contarProdutos();

        assertEquals(10, total);
        verify(produtoConsultaRepository).contaProdutos();
    }

    @Test
    @DisplayName("Deve retornar o valor total em estoque.")
    void retornaOValorTotalNoEstoque() {
        when(produtoConsultaRepository.calcularValorTotalEstoque()).thenReturn(10.0);

        double total = produtoRelatorioService.calcularValorTotalEstoque();

        assertEquals(10.0, total);
        verify(produtoConsultaRepository).calcularValorTotalEstoque();
    }

    @Test
    @DisplayName("Deve retornar o valor total de um certo produto no estoque.")
    void retornaOValorTotalDeUmProdutoNoEstoque() {
        Produto produto = new Produto("Fone", 70.00, 120, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);
        when(produtoConsultaRepository.calcularValorProduto(1)).thenReturn(10.0);

        double total = produtoRelatorioService.calcularValorProduto(1);

        assertEquals(10.0, total);
        verify(produtoRepository).buscar(1);
        verify(produtoConsultaRepository).calcularValorProduto(1);
    }

    @Test
    @DisplayName("Deve retornar a quantidade total de produtos ativos.")
    void deveRetornarQuantidadeTotalDeProdutosAtivos() {
        when(produtoConsultaRepository.contaProdutosAtivos()).thenReturn(10);
        int total = produtoRelatorioService.contaProdutosAtivos();

        assertEquals(10, total);
        verify(produtoConsultaRepository).contaProdutosAtivos();
    }

    @Test
    @DisplayName("Deve retornar a quantidade total de produtos inativos.")
    void deveRetornarQuantidadeTotalDeProdutosInativos() {
        when(produtoConsultaRepository.contaProdutosInativos()).thenReturn(10);
        int total = produtoRelatorioService.contaProdutosInativos();

        assertEquals(10, total);
        verify(produtoConsultaRepository).contaProdutosInativos();
    }

    @Test
    @DisplayName("Deve retornar a soma total das quantidades de produtos.")
    void deveRetornarASomaTotalDeProdutos() {
        when(produtoConsultaRepository.quantidadeTotalProdutos()).thenReturn(10);
        int total = produtoRelatorioService.somaQuantidadeProdutos();

        assertEquals(10, total);
        verify(produtoConsultaRepository).quantidadeTotalProdutos();
    }
}