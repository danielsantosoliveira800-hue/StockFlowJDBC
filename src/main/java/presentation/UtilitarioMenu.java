package presentation;

import service.ProdutoLoteService;

public class UtilitarioMenu {

    private final ProdutoLoteService produtoLoteService;
    private final LeitorEntrada leitor;

    public UtilitarioMenu(ProdutoLoteService produtoLoteService, LeitorEntrada leitor) {
        this.produtoLoteService = produtoLoteService;
        this.leitor = leitor;
    }

    public void exibir() {
        int opcao;

        do {
            System.out.println(" ");
            System.out.println(" ===MENU UTILITÁRIOS=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Inserir produtos em lote.");
            System.out.println("2- Testar savepoint.");
            System.out.println("0- Voltar.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = leitor.lerInteiro();

            switch (opcao) {
                case 1 -> inserirProdutosEmLote();
                case 2 -> produtoLoteService.testarSavepoint();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void inserirProdutosEmLote() {
        System.out.println("Quantos produtos deseja inserir?");
        int quantidade = leitor.lerInteiro();

        long inicio = System.currentTimeMillis();
        produtoLoteService.inserirProdutosEmLote(quantidade);
        long fim = System.currentTimeMillis();

        System.out.println("\n Produtos inseridos com sucesso!");
        System.out.println("Tempo: " + (fim - inicio) + " ms");
    }
}