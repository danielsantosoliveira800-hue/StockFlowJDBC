package presentation;

import service.AuditoriaProdutoService;
import service.MovimentacaoService;
import service.ProdutoLoteService;
import service.ProdutoRelatorioService;
import service.ProdutoService;

public class Menu {

    private final LeitorEntrada leitor;
    private final ProdutoMenu produtoMenu;
    private final MovimentacaoMenu movimentacaoMenu;
    private final RelatorioMenu relatorioMenu;
    private final AuditoriaMenu auditoriaMenu;
    private final UtilitarioMenu utilitarioMenu;

    public Menu(ProdutoService produtoService,
                ProdutoRelatorioService produtoRelatorioService,
                ProdutoLoteService produtoLoteService,
                MovimentacaoService movimentacaoService,
                AuditoriaProdutoService auditoriaProdutoService) {

        this.leitor = new LeitorEntrada();
        this.produtoMenu = new ProdutoMenu(produtoService, leitor);
        this.movimentacaoMenu = new MovimentacaoMenu(produtoService, movimentacaoService, leitor);
        this.relatorioMenu = new RelatorioMenu(produtoRelatorioService, leitor);
        this.auditoriaMenu = new AuditoriaMenu(auditoriaProdutoService);
        this.utilitarioMenu = new UtilitarioMenu(produtoLoteService, leitor);
    }

    public void exibir() {

        int opcao;

        do {
            System.out.println(" ");
            System.out.println(" ===STOCK FLOW=== ");
            System.out.println("-----------------------------------------");
            System.out.println("1- Produtos.");
            System.out.println("2- Movimentações.");
            System.out.println("3- Relatórios.");
            System.out.println("4- Auditoria.");
            System.out.println("5- Utilitários.");
            System.out.println("0- Sair.");
            System.out.println(" ");
            System.out.print("Escolha uma opção: ");
            System.out.println(" ");

            opcao = leitor.lerInteiro();

            switch (opcao) {
                case 1 -> produtoMenu.exibir();
                case 2 -> movimentacaoMenu.exibir();
                case 3 -> relatorioMenu.exibir();
                case 4 -> auditoriaMenu.exibir();
                case 5 -> utilitarioMenu.exibir();
                case 0 -> System.out.println("Encerrando o sistema.");
                default -> System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        leitor.fechar();
    }
}