package service;

import dao.MovimentacaoRepository;
import dao.ProdutoRepository;
import exception.EstoqueInsuficienteException;
import model.Movimentacao;
import model.Produto;
import model.StatusProduto;
import model.TipoMovimentacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import validation.MovimentacaoValidator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MovimentacaoServiceTest {

    @Mock
    private MovimentacaoRepository movimentacaoRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private MovimentacaoValidator movimentacaoValidator;
    @Mock
    private DataSource dataSource;
    @Mock
    private MovimentacaoService movimentacaoService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        movimentacaoService = new MovimentacaoService(
                movimentacaoRepository,
                produtoRepository,
                movimentacaoValidator,
                dataSource
        );
    }

    @Test
    @DisplayName("Deve lançar exception quando estoque estiver insuficiente.")

    void deveLancarExceptionQuandoEstoqueInsuficiente() throws Exception{

        Produto produto = new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO);
        produto.setId(1);

        Movimentacao movimentacao =  new Movimentacao(1, TipoMovimentacao.SAIDA,50);

        when(produtoRepository.buscar(any(Connection.class), eq(1))).thenReturn(produto);

        Connection connectionMock = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connectionMock);

        doThrow(new EstoqueInsuficienteException())
                .when(movimentacaoValidator)
                .validarSaida(produto, movimentacao);

        assertThrows(RuntimeException.class,
                () -> { movimentacaoService.registrarMovimentacao(movimentacao);});

        verify(connectionMock).rollback();

        verify(connectionMock, never()).commit();

        verify(connectionMock).close();
    }

    @Test
    @DisplayName("Deve lançar exception quando movimentação for invalida.")

    void deveLancarExceptionQuandoMovimentacaoForInvalida() throws SQLException {

        Produto produto = new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO);
        produto.setId(1);

        Movimentacao movimentacaoMock =  new Movimentacao();
        movimentacaoMock.setTipo(null);
        movimentacaoMock.setProduto_id(1);

        Connection connectionMock = mock(Connection.class);
        when(dataSource.getConnection()).thenReturn(connectionMock);

        when(produtoRepository.buscar(any(Connection.class),eq(1))).thenReturn(produto);

        assertThrows(RuntimeException.class, () -> {
            movimentacaoService.registrarMovimentacao(movimentacaoMock);
        });
    }

    @Test
    @DisplayName("Deve regstrar entrada de estoque com sucesso.")
    void deveResgistrarEntradaDeEstoqueComSucesso() throws SQLException {
        Produto produto = new Produto(
                "Teclado",
                120.0,
                40,
                StatusProduto.ATIVO
        );
        produto.setId(1);

        Movimentacao movimentacao = new Movimentacao(1, TipoMovimentacao.ENTRADA, 5);

        Connection connectionMock = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connectionMock);

        when(produtoRepository.buscar(any(Connection.class),eq(1))).thenReturn(produto);

        movimentacaoService.registrarMovimentacao(movimentacao);

        verify(produtoRepository).atualizarQuantidade(any(Connection.class),eq(1),eq(45));

        verify(movimentacaoRepository).registrarMovimentacao(any(Connection.class),eq(movimentacao));

        verify(connectionMock).commit();
    }

    @Test
    @DisplayName("Deve regstrar saida de estoque com sucesso.")
    void deveResgistrarSaidaDeEstoqueComSucesso() throws SQLException {
        Produto produto = new Produto(
                "Teclado",
                120.0,
                40,
                StatusProduto.ATIVO
        );
        produto.setId(1);

        Movimentacao movimentacao = new Movimentacao(1, TipoMovimentacao.SAIDA, 5);

        Connection connectionMock = mock(Connection.class);

        when(dataSource.getConnection()).thenReturn(connectionMock);

        when(produtoRepository.buscar(any(Connection.class),eq(1))).thenReturn(produto);

        movimentacaoService.registrarMovimentacao(movimentacao);

        verify(produtoRepository).atualizarQuantidade(any(Connection.class),eq(1),eq(35));

        verify(movimentacaoRepository).registrarMovimentacao(any(Connection.class),eq(movimentacao));

        verify(connectionMock).commit();
    }
}
