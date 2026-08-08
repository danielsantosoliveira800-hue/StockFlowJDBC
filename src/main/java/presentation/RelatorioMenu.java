package presentation;

import domain.model.Produto;
import domain.model.ProdutoRanking;
import domain.model.ResumoEstoque;
import service.ProdutoRelatorioService;
import util.CsvExporter;
import util.FormatadorUtil;

import java.util.List;

public class RelatorioMenu {

    private final ProdutoRelatorioService produtoRelatorioService;
    private final LeitorEntrada leitor;

    public RelatorioMenu(ProdutoRelatorioService produtoRelatorioService, LeitorEntrada leitor) {
        this.produtoRelatorioService = produtoRelatorioService;
        this.leitor = leitor;
    }

    public void exibir() {
        int opcao;

        do {
            System.out.println(" ");
            System.out.println(" ===MENU RELATÓRIOS=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Buscar produtos com estoque baixo.");
            System.out.println("2- Buscar produtos ativos.");
            System.out.println("3- Calcular valor total em estoque.");
            System.out.println("4- Exibir Dashboard.");
            System.out.println("5- Exibir ranking de produtos mais movimentados.");
            System.out.println("6- Calcular Valor Total de um produto.");
            System.out.println("7- Exportar CSV.");
            System.out.println("0- Voltar.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = leitor.lerInteiro();

            switch (opcao) {
                case 1 -> listarEstoqueBaixo();
                case 2 -> listarProdutosAtivos();
                case 3 -> exibirValorTotalEstoque();
                case 4 -> exibirDashboard();
                case 5 -> exibirProdutoRanking();
                case 6 -> calcularValorProduto();
                case 7 -> exportarCSV();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void listarEstoqueBaixo(){
        List<Produto> produtos = produtoRelatorioService.buscarEstoqueBaixo();

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto com estoque baixo.");
            return;
        }

        System.out.println(" ===PRODUTOS COM ESTOQUE BAIXO=== ");
        for (Produto produto : produtos) {
            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+ FormatadorUtil.formatadorMoeda(produto.getPreco()));
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");
        }
    }

    private void listarProdutosAtivos(){
        List<Produto> produtos = produtoRelatorioService.buscarEstoqueAtivo();

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto ativo.");
            return;
        }

        System.out.println(" ===PRODUTOS ATIVOS=== ");
        for (Produto produto : produtos) {
            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+FormatadorUtil.formatadorMoeda(produto.getPreco()));
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");
        }
    }

    private void exibirValorTotalEstoque(){
        double total = produtoRelatorioService.calcularValorTotalEstoque();

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.println(FormatadorUtil.formatadorMoeda(total));
    }

    private void exibirDashboard(){
        ResumoEstoque resumoEstoque = produtoRelatorioService.buscarResumoEstoque();
        List<Produto> estoqueBaixo = produtoRelatorioService.buscarEstoqueBaixo();

        System.out.println("\n === TOTAL DE PRODUTOS === ");
        System.out.println("Produtos cadastrados: "+ resumoEstoque.getTotalProdutos());

        System.out.println("\n === PRODUTOS ATIVOS === ");
        System.out.println("Produtos ativos: "+ resumoEstoque.getProdutosAtivos());

        System.out.println("\n === PRODUTOS INATIVOS === ");
        System.out.println("Produtos inativos: "+ resumoEstoque.getProdutosInativos());

        System.out.println("\n === QUANTIDADE TOTAL DE PRODUTOS === ");
        System.out.println("Quantidade total: "+ resumoEstoque.getQuantidadeTotalProdutos());

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.println("Valor: "+ FormatadorUtil.formatadorMoeda(resumoEstoque.getValorTotalEstoque()));

        System.out.println("\n === PRODUTOS COM ESTOQUE BAIXO === ");
        if (estoqueBaixo.isEmpty()){
            System.out.println("Não existem produtos com estoque baixo.");
        }else {
            for (Produto produto : estoqueBaixo) {
                System.out.println("Produto -> "+ produto.getNome() + ", Quantidade ->" + produto.getQuantidade() );
            }
        }
    }

    private void exibirProdutoRanking(){
        List<ProdutoRanking> rankingDeProdutos = produtoRelatorioService.buscarProdutoRanking();

        if (rankingDeProdutos.isEmpty()){
            System.out.println("Lista de ranking vazia.");
            return;
        }

        int posicao = 1;
        for (ProdutoRanking produtoRanking : rankingDeProdutos) {
            System.out.println("\n === "+ posicao +"º LUGAR ===");
            System.out.println(" ");
            System.out.println("Produto: "+ produtoRanking.getNomeProduto());
            System.out.println("Movimentações: "+ produtoRanking.getTotalMovimentacoes());
            System.out.println("Quantidade movimentada: "+ produtoRanking.getQuantidadeMovimentada());
            System.out.println("---------------------------------");
            System.out.println(" ");
            posicao++;
        }
    }

    private void calcularValorProduto() {
        System.out.println("Digite o id do produto: ");
        int id = leitor.lerInteiro();

        double valor = produtoRelatorioService.calcularValorProduto(id);

        System.out.println("Valor total do produto em estoque: "+ FormatadorUtil.formatadorMoeda(valor));
    }

    private void exportarCSV(){
        List<ProdutoRanking> ranking = produtoRelatorioService.buscarProdutoRanking();

        CsvExporter csvExporter = new CsvExporter();
        csvExporter.exportarRankingProdutos(ranking);
    }
}