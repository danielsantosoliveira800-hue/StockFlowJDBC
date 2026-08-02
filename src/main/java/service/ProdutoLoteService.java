package service;

import domain.model.Produto;
import domain.model.StatusProduto;
import domain.repository.ProdutoLoteRepository;

import java.util.ArrayList;
import java.util.List;

public class ProdutoLoteService {

    private final ProdutoLoteRepository produtoLoteRepository;

    public ProdutoLoteService(ProdutoLoteRepository produtoLoteRepository) {
        this.produtoLoteRepository = produtoLoteRepository;
    }

    public void inserirProdutosEmLote(int quantidade){

        List<Produto> produtos = new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            produtos.add(
                    new Produto(
                            "Produto teste " + i,
                            10.0 + i,
                            100,
                            StatusProduto.ATIVO
                    )
            );
        }
        produtoLoteRepository.inserirProdutoEmLote(produtos);
    }

    public void inserirProdutosComSavepoint(List<Produto> produtos){
        produtoLoteRepository.inserirProdutosComSavepoint(produtos);
    }

    public void testarSavepoint(){

        List<Produto> produtos = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            produtos.add(
                    new Produto(
                            "produto savepoint " + i,
                            100 + i,
                            10,
                            StatusProduto.ATIVO
                    )
            );
        }
        inserirProdutosComSavepoint(produtos);
    }
}