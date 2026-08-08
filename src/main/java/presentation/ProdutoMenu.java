package presentation;

import domain.model.Produto;
import domain.model.StatusProduto;
import service.ProdutoService;
import util.FormatadorUtil;

import java.util.List;

public class ProdutoMenu {

    private final ProdutoService produtoService;
    private final LeitorEntrada leitor;

    public ProdutoMenu(ProdutoService produtoService, LeitorEntrada leitor) {
        this.produtoService = produtoService;
        this.leitor = leitor;
    }

    public void exibir() {
        int opcao;

        do {
            System.out.println(" ");
            System.out.println(" ===MENU PRODUTOS=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Cadastrar produto.");
            System.out.println("2- Listar produtos.");
            System.out.println("3- Atualizar preço.");
            System.out.println("4- Desativar produto.");
            System.out.println("5- Buscar produto por id.");
            System.out.println("6- Buscar produto por nome.");
            System.out.println("0- Voltar.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = leitor.lerInteiro();

            switch (opcao) {
                case 1 -> salvarProduto();
                case 2 -> listarProdutos();
                case 3 -> atualizarProduto();
                case 4 -> desativarProduto();
                case 5 -> buscarProdutoPorId();
                case 6 -> buscarProdutoPorNome();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void salvarProduto() {
        System.out.println("Digite o nome do produto: ");
        String nome = leitor.lerString();

        System.out.println("Digite o preço do produto: R$");
        double preco = leitor.lerDouble();

        System.out.println("Digite a quantidade desse produto: ");
        int quantidade = leitor.lerInteiro();

        System.out.println("Digite o status desse produto: ");
        String statusStr = leitor.lerString();

        StatusProduto statusProduto;
        try {
            statusProduto = StatusProduto.valueOf(statusStr.trim().toUpperCase());
        }catch (IllegalArgumentException e){
            System.out.println("ERRO: "+e.getMessage());
            return;
        }

        Produto produto = new Produto(nome, preco, quantidade, statusProduto);

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
            System.out.println("Preço: R$"+ FormatadorUtil.formatadorMoeda(produto.getPreco()));
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");
        }
    }

    private void atualizarProduto() {
        try {
            System.out.println("id do produto: ");
            int id = leitor.lerInteiro();

            System.out.println("Novo preço: R$");
            double novoPreco = leitor.lerDouble();

            produtoService.atualizarPreco(id, novoPreco);
            System.out.println("Preço atualizado com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("Erro: produto não encontrado.");
        }
    }

    private void desativarProduto() {
        try {
            System.out.println("id do produto :");
            int id = leitor.lerInteiro();

            produtoService.desativar(id);
            System.out.println("Produto desativado com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("Erro: produto não encontrado.");
        }
    }

    private void buscarProdutoPorId() {
        System.out.println("Digite o id do produto: ");
        int id = leitor.lerInteiro();

        Produto produto = produtoService.buscarPorID(id);

        if (produto != null){
            System.out.println("\n ===PRODUTO ENCONTRADO===");
            System.out.println("ID: " + produto.getId());
            System.out.println("Nome: " + produto.getNome());
            System.out.println("Preço: " + FormatadorUtil.formatadorMoeda(produto.getPreco()));
            System.out.println("Quantidade: " + produto.getQuantidade());
            System.out.println("Status: " + produto.getStatus());
        } else {
            System.out.println("Produto não encontrado.");
        }
    }

    private void buscarProdutoPorNome(){
        System.out.println("Digite o nome do produto: ");
        String nome = leitor.lerString();

        List<Produto> produtos = produtoService.buscarPorNome(nome);

        if (produtos.isEmpty()){
            System.out.println("Nenhum produto encontrado.");
            return;
        }

        System.out.println("\n ===PRODUTOS ENCONTRADOS=== ");

        for (Produto produto : produtos) {
            System.out.println("ID: "+produto.getId());
            System.out.println("Nome: "+produto.getNome());
            System.out.println("Preço: R$"+FormatadorUtil.formatadorMoeda(produto.getPreco()));
            System.out.println("Quantidade: "+produto.getQuantidade());
            System.out.println("Status: "+produto.getStatus());
            System.out.println("------------------------------------");
        }
    }
}