package service;

import domain.model.Produto;
import domain.model.StatusProduto;
import domain.repository.ProdutoLoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

public class ProdutoLoteServiceTest {

    @Mock
    private ProdutoLoteRepository produtoLoteRepository;
    private ProdutoLoteService produtoLoteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        produtoLoteService = new ProdutoLoteService(produtoLoteRepository);
    }

    @Test
    @DisplayName("Deve inserir produtos em lote com a quantidade correta.")
    void inseriProdutosEmLoteComQuantidadeCorreta() {
        ArgumentCaptor<List<Produto>> captor = ArgumentCaptor.forClass(List.class);

        produtoLoteService.inserirProdutosEmLote(200);
        verify(produtoLoteRepository).inserirProdutoEmLote(captor.capture());

        List<Produto> produtosCapturados = captor.getValue();

        assertEquals(200, produtosCapturados.size());
    }

    @Test
    @DisplayName("Deve inserir produtos com savepoint.")
    void inserirProdutosComSavepoint() {
        List<Produto> produtos = List.of(
                new Produto("Teclado", 135.0, 50, StatusProduto.ATIVO)
        );

        produtoLoteService.inserirProdutosComSavepoint(produtos);
        verify(produtoLoteRepository).inserirProdutosComSavepoint(produtos);
    }
}