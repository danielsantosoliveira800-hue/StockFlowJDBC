package ui;

import Service.ProdutoService;
import model.Produto;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class Menu {
    private Scanner sc = new Scanner(System.in);

    private ProdutoService service = new ProdutoService();

    public void exibir() {

        int opcao;

        do {

            System.out.println(" ");
            System.out.println(" ===STOCK FLOW=== ");
            System.out.println("1- Cadastrar produto.");
            System.out.println("2- Listar produtos.");
            System.out.println("3- Atualizar preço.");
            System.out.println("4- Deletar produto.");
            System.out.println("5- Buscar produto por id. ");
            System.out.println("6- Buscar produto por nome.");
            System.out.println("7- Entrada de estoque.");
            System.out.println("8- Saida de estoque.");
            System.out.println("9- Buscar produtos com estoue baixo.");
            System.out.println("10- Buscar produtos ativos.");
            System.out.println("11- Calcular valor total em estoque.");
            System.out.println("12- Exibir Dashboard.");
            System.out.println("13- Sair.");
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
                    System.out.println("Encerrando o sistema.");
                }
                default -> System.out.println("Opção inválida.");
            }
        }while (opcao != 13);

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
        String status = lerString();

        Produto produto = new Produto(
                nome,
                preco,
                quantidade,
                status);
        try {
            service.cadastrarProduto(produto);

        }catch (IllegalArgumentException e){
            System.out.println("ERRO: "+e.getMessage());
        }
    }

    private void listarProdutos() {
        List<Produto> produtos = service.listar();

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
        System.out.println("id do produto: ");
        int id = lerInteiro();

        System.out.println("Novo preço: R$");
        double novoPreco = lerDouble();

        service.atualizarPreco(id,novoPreco);
    }

    private void deletarProduto() {
        System.out.println("id do produto :");
        int id = lerInteiro();

        service.deletar(id);
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

        Produto produto = service.buscarPorID(id);

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

        List<Produto> produtos = service.buscarPorNome(nome);

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

        System.out.println("Digite a nova a quantidade a adicionar no estoque: ");
        int quantidade = lerInteiro();

        try {
            service.entradaEstoque(id,quantidade);

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
            service.saidaEstoque(id, quantidade);

            System.out.println("Saida realizada com sucesso.");

        }catch (IllegalArgumentException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }

    private void listarEstoqueBaixo(){

        List<Produto> produtos =
                service.buscarEstoqueBaixo();

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
        List<Produto> produtos = service.buscarEstoqueAtivo();

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
        double total = service.calcularValorTotalEstoque();

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.printf("R$ %.2f%n", total);
    }

    private void exibirDashboard(){
        int totalProdutos = service.contarProdutos();
        int totalProdutosAtivos = service.contaProdutosAtivos();
        int totalProdutosInativos = service.contaProdutosInativos();
        int quantidadeTotalProdutos = service.somaQuantidadeProdutos();
        double valorDoEstoque = service.calcularValorTotalEstoque();
        List<Produto> estoqueBaixo = service.buscarEstoqueBaixo();

        System.out.println("\n === TOTAL DE PRODUTOS === ");
        System.out.println("Produtos cadastrados: "+ totalProdutos);

        System.out.println("\n === PRODUTOS ATIVOS === ");
        System.out.println("Produtos ativos: "+ totalProdutosAtivos);

        System.out.println("\n === PRODUTOS INATIVOS === ");
        System.out.println("Produtos inativos: "+ totalProdutosInativos);

        System.out.println("\n === QUANTIDADE TOTAL DE PRODUTOS === ");
        System.out.println("Quantidade total: "+ quantidadeTotalProdutos);

        System.out.println("\n === VALOR TOTAL DO ESTOQUE === ");
        System.out.printf("Valor: R$ %.2f%n",valorDoEstoque);

        System.out.println("\n === PRODUTOS COM ESTOQUE BAIXO === ");
        if (estoqueBaixo.isEmpty()){
            System.out.println("Não existem produtos com estoque baixo.");
        }else {
            for (Produto produto : estoqueBaixo) {
                System.out.println("Produto -> "+ produto.getNome() + ", Quantidade ->" + produto.getQuantidade() );
            }
        }
    }
}
