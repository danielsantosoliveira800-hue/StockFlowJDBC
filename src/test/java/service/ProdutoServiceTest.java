package service;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import dao.ProdutoRepository;
import exception.ProdutoNaoEncontradoException;
import exception.ValidacaoException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.LoggerFactory;
import validation.ProdutoValidator;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import ch.qos.logback.classic.Logger;
import java.sql.SQLException;
import java.util.ArrayList;
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
    @DisplayName("Deve retornar o resumo do estoque.")
    void deveRetornarResumoDoEstoque() {
        ResumoEstoque resumoMock = new ResumoEstoque();
        resumoMock.setProdutosInativos(30);
        resumoMock.setProdutosAtivos(970);
        resumoMock.setTotalProdutos(1000);
        resumoMock.setQuantidadeTotalProdutos(3056);
        resumoMock.setValorTotalEstoque(860996.00);

        when(produtoRepository.buscarResumoEstoque()).thenReturn(resumoMock);

        ResumoEstoque resultado = produtoService.buscarResumoEstoque();

        // confira pelo menos 1 ou 2 campos com assertEquals
        // e faça o verify no repository

        assertEquals(30, resultado.getProdutosInativos());
        assertEquals(970, resultado.getProdutosAtivos());
        verify(produtoRepository).buscarResumoEstoque();
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
    @DisplayName("Deve retornar uma lista de produtos com estoque baixo. ")
    void retornaListaDeProdutosComEstoeueBaixo() {
        List<Produto> estoqueBaixoMock = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoRepository.buscarEstoqueBaixo()).thenReturn(estoqueBaixoMock);

        List<Produto> resultado = produtoService.buscarEstoqueBaixo();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoRepository).buscarEstoqueBaixo();
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos com estoque ativo.")
    void retornaListaDeProdutosAtivos() {
        List<Produto> produtosAtivos = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoRepository.buscarProdutosAtivos()).thenReturn(produtosAtivos);

        List<Produto> resultado = produtoService.buscarEstoqueAtivo();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoRepository).buscarProdutosAtivos();
    }

    @Test
    @DisplayName("Deve retoenar um ranking de produtos.")
    void retornaRankingdeProdutos() {
        List<ProdutoRanking> rankingMock = List.of(
                new ProdutoRanking("Teclado", 12, 340)
        );

        when(produtoRepository.buscarRankingProdutos()).thenReturn(rankingMock);

        List<ProdutoRanking> resultado = produtoService.buscarProdutoRanking();

        assertEquals(1, resultado.size());
        assertEquals("Teclado", resultado.get(0).getNomeProduto());
        verify(produtoRepository).buscarRankingProdutos();
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
    void deveRetornarProdutoDesativadoComSucesso() {
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
    @DisplayName("Deve retornar o valor total em estoque.")
    void retornaOValorTotalNoEstoque() {
        when(produtoRepository.calcularValorTotalEstoque()).thenReturn(10.0);

        double total = produtoService.calcularValorTotalEstoque();

        assertEquals(10.0, total);
        verify(produtoRepository).calcularValorTotalEstoque();
    }


    @Test
    @DisplayName("Deve retornar o valor total de um certo produto no estoque.")
    void retornaOValorTotalDeUmProdutoNoEstoque() {
        Produto produto = new Produto("Fone", 70.00, 120, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);
        when(produtoRepository.calcularValorProduto(1)).thenReturn(10.0);

        double total = produtoService.calcularValorProduto(1);

        assertEquals(10.0, total);
        verify(produtoRepository).buscar(1);
        verify(produtoRepository).calcularValorProduto(1);
    }

    @Test
    @DisplayName("Deve retornar a quantidade total de produtos ativos.")
    void deveRetornarQuantidadeTotalDeProdutosAtivos() {
        when(produtoRepository.contaProdutosAtivos()).thenReturn(10);
        int total = produtoService.contaProdutosAtivos();

        assertEquals(10, total);
        verify(produtoRepository).contaProdutosAtivos();
    }

    @Test
    @DisplayName("Deve retornar a quantidade total de produtos inativos.")
    void deveRetornarQuantidadeTotalDeProdutosInativos() {
        when(produtoRepository.contaProdutosInativos()).thenReturn(10);
        int total = produtoService.contaProdutosInativos();

        assertEquals(10, total);
        verify(produtoRepository).contaProdutosInativos();
    }

    @Test
    @DisplayName("Deve retornar a soma total das quantidades de produtos.")
    void deveRetornarASomaTotalDeProdutos() {
        when(produtoRepository.quantidadeTotalProdutos()).thenReturn(10);
        int total = produtoService.somaQuantidadeProdutos();

        assertEquals(10, total);
        verify(produtoRepository).quantidadeTotalProdutos();
    }

    @Test
    @DisplayName("Deve inserir produtos em lote com a quantidade correta.")
    void inseriProdutosEmLoteComQuantidadeCorreta() {
        ArgumentCaptor<List<Produto>> captor = ArgumentCaptor.forClass(List.class);

        produtoService.inserirProdutosEmLote(200);
        verify(produtoRepository).inserirProdutoEmLote(captor.capture());

        List<Produto> produtosCapturados = captor.getValue();

        assertEquals(200, produtosCapturados.size());

    }

    @Test
    @DisplayName("Deve inserir produtos com savepoint.")
    void inserirProdutosComSavepoint() {
        List<Produto> produtos = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        produtoService.inserirProdutosComSavepoint(produtos);
        verify(produtoRepository).inserirProdutosComSavepoint(produtos);
    }

    @Test
    @DisplayName("Deve definir status INATIVO ao cadastrar produto com quantidade zero.")
    void deveDefinirStatusInativoQuandoQuantidadeForZero() {
        Produto produto = new Produto("Monitor", 900.0, 0, StatusProduto.ATIVO);

        produtoService.cadastrarProduto(produto);

        assertEquals(StatusProduto.INATIVO, produto.getStatus());
        verify(produtoRepository).salvarProduto(produto);
    }

    @Test
    @DisplayName("Deve definir status ATIVO ao cadastrar produto com quantidade positiva.")
    void deveDefinirStatusAtivoQuandoQuantidadeForPositiva() {
        Produto produto = new Produto("Monitor", 900.0, 20, StatusProduto.INATIVO);

        produtoService.cadastrarProduto(produto);

        assertEquals(StatusProduto.ATIVO, produto.getStatus());
        verify(produtoRepository).salvarProduto(produto);
    }

    @Test
    @DisplayName("Deve reativar produto com sucesso.")
    void deveReativarProdutoComSucesso(){
        Produto produto = new Produto("Mouse", 40.0, 150, StatusProduto.INATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.reativar(1);

        verify(produtoRepository).buscar(1);
        verify(produtoRepository).reativar(1);
    }

    @Test
    @DisplayName("Deve registrar log de auditoria ao desativar produto.")
    void deveLogarAuditoriaAoDesativarProduto(){

        Logger auditLogger = (Logger) LoggerFactory.getLogger("AUDIT");
        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        auditLogger.addAppender(listAppender);

        Produto produto = new Produto("Mouse", 40.00, 150, StatusProduto.ATIVO);
        produto.setId(1);
        when(produtoRepository.buscar(1)).thenReturn(produto);

        produtoService.desativar(1);

        List<ILoggingEvent> logs = listAppender.list;

        boolean encontrouLogAuditoria = logs.stream()
                .anyMatch(evento -> evento.getFormattedMessage()
                        .contains("Produto desativado manualmente   ")
                             && evento.getFormattedMessage().contains("id=1"));

        logs.forEach(evento -> System.out.println("[" + evento.getLevel() + "] " + evento.getFormattedMessage()));
        assertTrue(encontrouLogAuditoria);
        auditLogger.detachAppender(listAppender);
    }
}