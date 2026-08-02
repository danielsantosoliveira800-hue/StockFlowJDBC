package service;

import infrastructure.persistence.MovimentacaoDAO;
import infrastructure.persistence.ProdutoDAO;
import infrastructure.ConnectionFactory;
import integration.IntegrationTestBase;
import domain.model.Movimentacao;
import domain.model.Produto;
import domain.model.StatusProduto;
import domain.model.TipoMovimentacao;
import org.junit.jupiter.api.Test;
import domain.MovimentacaoValidator;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConcorrenciaMovimentacaoTest extends IntegrationTestBase {

    @Test
    void deveManterEstoqueCorretoSobConcorrencia() throws InterruptedException {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        MovimentacaoService movimentacaoService = new MovimentacaoService(
                new MovimentacaoDAO(),
                produtoDAO,
                new MovimentacaoValidator(),
                ConnectionFactory.getDataSource()
        );

        Produto produto = new Produto("Produto Concorrente", 50.0, 100, StatusProduto.ATIVO);
        produtoDAO.salvarProduto(produto);

        int numeroDeThreads = 10;
        int quantidadePorVenda = 10;

        ExecutorService executor = Executors.newFixedThreadPool(numeroDeThreads);
        CountDownLatch largada = new CountDownLatch(1);
        CountDownLatch chegada = new CountDownLatch(numeroDeThreads);

        for (int i = 0; i < numeroDeThreads; i++) {
            executor.submit(()->{
                try {
                    largada.await();
                    Movimentacao movimentacao = new Movimentacao(1, TipoMovimentacao.SAIDA, quantidadePorVenda);
                    movimentacaoService.registrarMovimentacao(movimentacao);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt();
                }finally {
                    chegada.countDown();
                }
            });
        }
        largada.countDown();
        chegada.await();
        executor.shutdown();

        Produto produtoFinal = produtoDAO.buscar(1);
        assertEquals(0, produtoFinal.getQuantidade());
    }
}
