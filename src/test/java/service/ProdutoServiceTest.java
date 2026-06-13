package service;

import dao.ProdutoRepository;
import dao.MovimentacaoRepository;
import exception.ProdutoNaoEncontradoException;
import model.Produto;
import model.StatusProduto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import validation.ProdutoValidation;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProdutoServiceTest {
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private MovimentacaoService movimentacaoService;
    @Mock
    private ProdutoValidation produtoValidation;
    private ProdutoService produtoService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        produtoService = new ProdutoService(movimentacaoService, produtoRepository, produtoValidation);
    }

    @Test
    @DisplayName("Deve retornar uma lista de produtos.")

    void deveRertornarListaDeProdutos(){
        List<Produto> produtosMock = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        when(produtoRepository.listar()).thenReturn(produtosMock);

        List<Produto> resultado = produtoService.listar();

        assertEquals(1,resultado.size());
        assertEquals("Teclado", resultado.get(0).getNome());
        verify(produtoRepository).listar();
    }

    @Test
    @DisplayName("Deve retornar produto.")

    void deveRetornarProduto(){
        Produto produtoMock = new Produto("Fone", 75.0, 45, StatusProduto.ATIVO);

        when(produtoRepository.buscar(999)).thenReturn(null);

        assertThrows(ProdutoNaoEncontradoException.class, () -> produtoService.buscarPorID(999));

    }
}
