package service;

import dao.ProdutoRepository;
import exception.ProdutoNaoEncontradoException;
import exception.ValidacaoException;
import model.Movimentacao;
import model.Produto;
import model.StatusProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import validation.ProdutoValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private MovimentacaoService movimentacaoService;
    @Mock
    private ProdutoValidator produtoValidator;
    private ProdutoService produtoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(movimentacaoService, produtoRepository, produtoValidator);
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos.")
    void deveRertornarListaDeProdutos() {
        List<Produto> produtosMock = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoRepository.listar()).thenReturn(produtosMock);

        List<Produto> resultado = produtoService.listar();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoRepository).listar();
    }

    @Test
    @DisplayName("Deve atuslizar preço com sucesso.")
    void deveRetorrnarPrecoComSucesso() {
        Produto produto = new Produto("Mouse", 75.0, 30, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.atualizarPreco(1, 65.0);

        verify(produtoValidator).atualizarPreco(65.0);
        verify(produtoRepository).buscar(1);
        verify(produtoRepository).atualizar(1, 65.0);
    }

    @Test
    @DisplayName("Deve desativar produto com sucesso.")
    void deveRetornarProdutoComSucesso() {
        Produto produto = new Produto("Mouse", 40.0, 150, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.desativar(1);

        verify(produtoRepository).buscar(1);
        verify(produtoRepository).desativar(1);
    }

    @Test
    @DisplayName("Deve retornar exceção quando produto não existir.")
    void deveRetonarExceptionQuandoProdutoNaoExistir() {

        when(produtoRepository.buscar(anyInt())).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorID(999));

    }

    @Test
    @DisplayName("Deve lançar exceção quando produto for inválido.")
    void deveLancarExceptionQuandoIdForInvalido() {
        assertThrows(ValidacaoException.class, () -> produtoService.buscarPorID(0));

        verify(produtoRepository, never()).buscar(anyInt());
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso.")
    void deveRetornarProdutoCadastrado() {

        Produto produto = new Produto("Hadset", 230.0, 20, StatusProduto.ATIVO);

        produtoService.cadastrarProduto(produto);

        verify(produtoValidator).validarProduto(produto);
        verify(produtoRepository).salvarProduto(produto);

    }

    @Test
    @DisplayName("Deve reotornar produto em busca por nome.")
    void deveRetornarProdutoEmBuscaPorNome() {
        List<Produto> produtos = List.of(new Produto("mouse", 45.0, 70, StatusProduto.ATIVO));
        when(produtoRepository.buscarPorNome("mouse")).thenReturn(produtos);

        List<Produto> resultado = produtoService.buscarPorNome("mouse");

        verify(produtoValidator).validarNome("mouse");
        verify(produtoRepository).buscarPorNome("mouse");

        assertEquals(1, resultado.size());
        assertEquals("mouse", resultado.get(0).getNome());
    }

    @Test
    @DisplayName("Deve mostrar entrada efetuada com sucesso e movimentção registrada.")
    void entradaComSucessoEMovementacaoRegistrada() {
        Produto produto = new Produto("Notebook", 5670.00, 30, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.entradaEstoque(1, 15);

        verify(produtoValidator).validarQuantidade(15);
        verify(movimentacaoService).registrarMovimentacao(any(Movimentacao.class));
    }


    @Test
    @DisplayName("Deve mostrar saída efetuada com sucesso e movimentção registrada.")
    void saidaComSucessoEMovementacaoRegistrada() {
        Produto produto = new Produto("Notebook", 5670.00, 30, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.saidaEstoque(1, 15);

        verify(produtoValidator).validarQuantidade(15);
        verify(movimentacaoService).registrarMovimentacao(any(Movimentacao.class));
    }

    @Test
    @DisplayName("deve retornar a quantidade de produtos.")
    void deveRetornarAQuantidadeDeProdutos() {
        when(produtoRepository.contaProdutos()).thenReturn(10);

        int total = produtoService.contarProdutos();

        assertEquals(10, total);
        verify(produtoRepository).contaProdutos();
    }

    @Test
    @DisplayName("Deve retornar a quantidade total de produtos ativos.")
    void deveRetornarQuantidadeTotalDeProdutosAtivos() {
        when(produtoRepository.contaProdutosAtivos()).thenReturn(10);
        int total = produtoService.contaProdutosAtivos();

        assertEquals(10, total);
        verify(produtoRepository).contaProdutosAtivos();
    }
}