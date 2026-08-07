package presentation;

import domain.model.Movimentacao;
import service.MovimentacaoService;
import service.ProdutoService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MovimentacaoMenu {

    private final ProdutoService produtoService;
    private final MovimentacaoService movimentacaoService;
    private final LeitorEntrada leitor;

    public MovimentacaoMenu(ProdutoService produtoService, MovimentacaoService movimentacaoService, LeitorEntrada leitor) {
        this.produtoService = produtoService;
        this.movimentacaoService = movimentacaoService;
        this.leitor = leitor;
    }

    public void exibir() {
        int opcao;

        do {
            System.out.println(" ");
            System.out.println(" ===MENU MOVIMENTAÇÕES=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Entrada de estoque.");
            System.out.println("2- Saída de estoque.");
            System.out.println("3- Exibir movimentações.");
            System.out.println("4- Buscar movimentações por data.");
            System.out.println("0- Voltar.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = leitor.lerInteiro();

            switch (opcao) {
                case 1 -> entradaEstoque();
                case 2 -> saidaEstoque();
                case 3 -> exibirMovimentacoes();
                case 4 -> exibirMovimentacoesPorData();
                case 0 -> System.out.println("Voltando ao menu principal.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);
    }

    private void entradaEstoque(){
        System.out.println("Digite o id do produto: ");
        int id = leitor.lerInteiro();

        System.out.println("Digite a nova a quantidade adicionar no estoque: ");
        int quantidade = leitor.lerInteiro();

        try {
            produtoService.entradaEstoque(id,quantidade);
            System.out.println("Entrada realizada com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("Erro: "+ e.getMessage());
        }
    }

    private void saidaEstoque(){
        System.out.println("Digite o id do produto: ");
        int id = leitor.lerInteiro();

        System.out.println("Digite a quantidade a sair do produto: ");
        int quantidade = leitor.lerInteiro();

        try {
            produtoService.saidaEstoque(id, quantidade);
            System.out.println("Saida realizada com sucesso.");
        }catch (IllegalArgumentException e){
            System.out.println("Erro: "+e.getMessage());
        }
    }

    private void exibirMovimentacoes(){
        List<Movimentacao> movimentacoes = movimentacaoService.listarMovimentacoes();
        exibirListaMovimentacoes(movimentacoes);
    }

    private void exibirMovimentacoesPorData(){
        try {
            System.out.println("Digite a data de inicio da análise: ");
            String dataInicio = leitor.lerString();

            System.out.println("Digite a data de fim da análise: ");
            String dataFim = leitor.lerString();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            LocalDate inicio = LocalDate.parse(dataInicio, formatter);
            LocalDate fim = LocalDate.parse(dataFim, formatter);

            if (inicio.isAfter(fim)){
                System.out.println("A data de inicio não pode ser maior que a data final.");
                return;
            }

            List<Movimentacao> movimentacoesPorPeriodo = movimentacaoService.buscarMovimentacoesPorData(inicio, fim);
            exibirListaMovimentacoes(movimentacoesPorPeriodo);

        }catch (DateTimeParseException e){
            System.out.println("Formato de data inválida, por favor utilize (DD/MM/AAAA)");
        }
    }

    private void exibirListaMovimentacoes(List<Movimentacao> movimentacoes){
        if (movimentacoes.isEmpty()){
            System.out.println("Nâo existe histórico de movimentações.");
            return;
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
}