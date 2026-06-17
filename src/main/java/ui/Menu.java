package ui;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import model.*;
import service.MovimentacaoService;
import service.ProdutoService;
import util.CsvExporter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Menu {
    private Scanner sc = new Scanner(System.in);

    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;

    public Menu(ProdutoService produtoService, MovimentacaoService movimentacaoService) {
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
    }

    public void exibir() {

        int opcao;

        do {

            System.out.println(" ");
            System.out.println(" ===STOCK FLOW=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Cadastrar produto.");
            System.out.println("2- Listar produtos.");
            System.out.println("3- Atualizar preço.");
            System.out.println("4- Deletar produto.");
            System.out.println("5- Buscar produto por id. ");
            System.out.println("6- Buscar produto por nome.");
            System.out.println("7- Entrada de estoque.");
            System.out.println("8- Saida de estoque.");
            System.out.println("9- Buscar produtos com estoque baixo.");
            System.out.println("10- Buscar produtos ativos.");
            System.out.println("11- Calcular valor total em estoque.");
            System.out.println("12- Exibir Dashboard.");
            System.out.println("13- Exibir movimentações.");
            System.out.println("14- Buscar movimentações por data.");
            System.out.println("15- Exibir ranking de produtos mais movimentados.");
            System.out.println("16- Exportar CSV.");
            System.out.println("17- Sair.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = lerInteiro();

            switch (opcao){
                case 1 -> {
                    salvarProduto();
                }
                case 2 ->{
                    listarProdutos();
                }
                case 3 -> {
                    atualizarProduto();
                }
                case 4 -> {
                    deletarProduto();
                }
                case 5 ->{
                    buscarProdutoPorId();
                }
                case 6 ->{
                    buscarProdutoPorNome();
                }
                case 7 ->{
                    entradaEstoque();
                }
                case 8 -> {
                    saidaEstoque();
                }
                case 9 ->{
                    listarEstoqueBaixo();
                }
                case 10 ->{
                    listarProdutosAtivos();
                }
                case 11 ->{
                    exibirValorTotalEstoque();
                }
                case 12 ->{
                    exibirDashboard();
                }
                case 13 -> {
                    exibirMovimentacoes();
                }
                case 14 ->{
                    exibirMovimentacoesPorData();
                }
                case 15 -> {
                    exibirProdutoRanking();
                }
                case 16 ->{
                    exportarCSV();
                }
                case 17->{
                    System.out.println("Encerrando o sistema.");
                }
                default -> System.out.println("Opção inválida.");
            }
        }while (opcao != 17);

        sc.close();
    }

    private void salvarProduto() {

        System.out.println("Digite o nome do produto: ");
        String nome = lerString();

        System.out.println("Digite o preço do produto: R$");
        double preco = lerDouble();

        System.out.println("Digite a quantidade desse produto: ");
        int quantidade = lerInteiro();

        System.out.println("Digite o status desse produto: ");
        String statusStr = lerString();

        StatusProduto statusProduto;
        try {
            statusProduto = StatusProduto.valueOf(statusStr.trim().toUpperCase());
        }catch (IllegalArgumentException e){
            System.out.println("ERRO: "+e.getMessage());
            return;
        }

        Produto produto = new Produto(
                nome,
                preco,
                quantidade,
                statusProduto);

        try {
            produtoService.cadastrarProduto(produto);
            System.out.println("Produto cadastrado com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("ERRO: "+e.getMessage());
        }
    }

    private void listarProdutos() {
        List<Produto> produtos = produtoService.listar();

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto cadastrado.");
            return;
        }

        System.out.println(" ===PRODUTOS CADASTRADOS=== ");

        for (Produto produto : produtos) {
            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+produto.getPreco());
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");

        }
    }

    private void atualizarProduto() {
        try {
            System.out.println("id do produto: ");

            int id = lerInteiro();

            System.out.println("Novo preço: R$");
            double novoPreco = lerDouble();

            produtoService.atualizarPreco(id, novoPreco);
            System.out.println("Preço atualizado com sucesso.");

        }catch (IllegalArgumentException e){
            System.out.println("Erro: produto não encontrado.");
        }
    }

    private void deletarProduto() {
        try {
            System.out.println("id do produto :");

            int id = lerInteiro();

            produtoService.deletar(id);
            System.out.println("Produto deletado com sucesso.");

        }catch (IllegalArgumentException e){
            System.out.println("Erro: produto não encontrado.");
        }
    }

    private int lerInteiro() {
        while (true){
            try {

                int valor = sc.nextInt();

                sc.nextLine();

                return valor;
            }catch (InputMismatchException e){
                System.out.println("Digite um valor inteiro.");
                sc.nextLine();
            }
        }
    }

    private double lerDouble() {
        while (true){
            try {

                double valor = sc.nextDouble();

                sc.nextLine();

                return valor;
            }catch (InputMismatchException e){
                System.out.println("Digite um valor válido.");
                sc.nextLine();
            }
        }
    }

    private String lerString(){
        while (true){
            try {
                String texto = sc.nextLine();

                if (! texto.trim().isEmpty()){

                    return texto;}

            }catch (InputMismatchException e){
                System.out.println("Texto inválido, tente novamente.");
            }
        }
    }

    private void buscarProdutoPorId() {
        System.out.println("Digite o id do produto: ");

        int id = lerInteiro();

        Produto produto = produtoService.buscarPorID(id);

        if (produto != null){
            System.out.println("\n ===PRODUTO ENCONTRADO===");

            System.out.println(
                    "ID: " + produto.getId()
            );

            System.out.println(
                    "Nome: " + produto.getNome()
            );

            System.out.println(
                    "Preço: " + produto.getPreco()
            );

            System.out.println(
                    "Quantidade: " +
                            produto.getQuantidade()
            );

            System.out.println(
                    "Status: " +
                            produto.getStatus()
            );

        } else {

            System.out.println(
                    "Produto não encontrado."
            );
        }
    }

    private void buscarProdutoPorNome(){
        System.out.println("Digite o nome do produto: ");
        String nome = lerString();

        List<Produto> produtos = produtoService.buscarPorNome(nome);

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto encontrado.");

            return;
        }

        System.out.println("\n ===PRODUTOS ENCONTRADOS=== ");

        for (Produto produto : produtos) {
            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+produto.getPreco());
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");
        }
    }

    private void entradaEstoque(){
        System.out.println("Digite o id do produto: ");
        int id = lerInteiro();

        System.out.println("Digite a nova a quantidade adicionar no estoque: ");
        int quantidade = lerInteiro();

        try {
            produtoService.entradaEstoque(id,quantidade);

            System.out.println("Entrada realizada com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("Erro: "+ e.getMessage());
        }
    }

    private void saidaEstoque(){
        System.out.println("Digite o id do produto: ");
        int id = lerInteiro();

        System.out.println("Digite a quantidade a sair do produto: ");
        int quantidade = lerInteiro();

        try {
            produtoService.saidaEstoque(id, quantidade);

            System.out.println("Saida realizada com sucesso.");

        }catch (IllegalArgumentException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }

    private void listarEstoqueBaixo(){

        List<Produto> produtos =
                produtoService.buscarEstoqueBaixo();

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto com estoque baixo.");
            return;
        }

        System.out.println(" ===PRODUTOS COM ESTOQUE BAIXO=== ");

        for (Produto produto : produtos) {

            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+produto.getPreco());
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");

        }
    }

    private void listarProdutosAtivos(){
        List<Produto> produtos = produtoService.buscarEstoqueAtivo();

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto ativo.");
            return;
        }

        System.out.println(" ===PRODUTOS ATIVOS=== ");

        for (Produto produto : produtos) {

            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+produto.getPreco());
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");

        }
    }

    private void exibirValorTotalEstoque(){
        double total = produtoService.calcularValorTotalEstoque();

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.printf("R$ %.2f%n", total);
    }

    private void exibirDashboard(){

        ResumoEstoque resumoEstoque = produtoService.buscarResumoEstoque();

        List<Produto> estoqueBaixo = produtoService.buscarEstoqueBaixo();

        System.out.println("\n === TOTAL DE PRODUTOS === ");
        System.out.println("Produtos cadastrados: "+ resumoEstoque.getTotalProdutos());

        System.out.println("\n === PRODUTOS ATIVOS === ");
        System.out.println("Produtos ativos: "+ resumoEstoque.getProdutosAtivos());

        System.out.println("\n === PRODUTOS INATIVOS === ");
        System.out.println("Produtos inativos: "+ resumoEstoque.getProdutosInativos());

        System.out.println("\n === QUANTIDADE TOTAL DE PRODUTOS === ");
        System.out.println("Quantidade total: "+ resumoEstoque.getQuantidadeTotalProdutos());

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.printf("Valor: R$ %.2f%n",resumoEstoque.getValorTotalEstoque());

        System.out.println("\n === PRODUTOS COM ESTOQUE BAIXO === ");
        if (estoqueBaixo.isEmpty()){
            System.out.println("Não existem produtos com estoque baixo.");
        }else {
            for (Produto produto : estoqueBaixo) {
                System.out.println("Produto -> "+ produto.getNome() + ", Quantidade ->" + produto.getQuantidade() );
            }
        }
    }

    private void exibirMovimentacoes(){
        List<Movimentacao> movimentacoes = movimentacaoService.listarMovimentacoes();

        exibirListaMovimentacoes(movimentacoes);
    }

    private void exibirMovimentacoesPorData(){
       try {
           System.out.println("Digite a data de inicio da análise: ");

        String dataInicio = lerString();

        System.out.println("Digite a data de fim da análise: ");
        String dataFim = lerString();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

           LocalDate inicio = LocalDate.parse(dataInicio, formatter);

           LocalDate fim = LocalDate.parse(dataFim, formatter);

           if (inicio.isAfter(fim)){
               System.out.println("A data de inicio não pode ser maior que a data final.");
               return;
           }

        List<Movimentacao> movimentacoesPorPeriodo = movimentacaoService.buscarMovimentacoesPorData(inicio , fim);

           exibirListaMovimentacoes(movimentacoesPorPeriodo);

       }catch (DateTimeParseException e){

           System.out.println("Formato de data inválida, por favor utilize (DD/MM/AAAA)");
       }
    }

    private void exibirListaMovimentacoes (List<Movimentacao> movimentacoes){

        if (movimentacoes.isEmpty()){
            System.out.println("Nâo existe histórico de movimentações.");
            return ;
        }

        System.out.println(" ");
        System.out.println("\n === MOVIMENTAÇÕES CADASTRADAS === ");

        for (Movimentacao movimentacao: movimentacoes) {

            System.out.println(" ");
            System.out.println("Nome do produto: "+ movimentacao.getNomeProduto());
            System.out.println("Tipo de movimentação: "+ movimentacao.getTipo());
            System.out.println("Quantidade: "+ movimentacao.getQuantidade());
            System.out.println("Data da movimentacao: "+ movimentacao.getDataMovimentacao());
            System.out.println("----------------------------------------------------------");

        }
    }

    private void exibirProdutoRanking(){
        List<ProdutoRanking> rankingDeProdutos = produtoService.buscarProdutoRanking();

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

    private void exportarCSV(){
        List<ProdutoRanking> ranking = produtoService.buscarProdutoRanking();

        CsvExporter csvExporter = new CsvExporter();

        csvExporter.exportarRankingProdutos(ranking);
    }
}
