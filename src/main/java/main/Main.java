package main;

import infrasctructure.persistence.MovimentacaoDAO;
import infrasctructure.persistence.ProdutoDAO;
import infrasctructure.ConnectionFactory;
import infrasctructure.DashboardSnapshotScheduler;
import service.AuditoriaProdutoService;
import service.MovimentacaoService;
import service.ProdutoService;
import ui.Menu;
import domain.MovimentacaoValidator;
import domain.ProdutoValidator;

public class Main {
    public static void main(String[] args) {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
        MovimentacaoValidator movimentacaoValidator =  new MovimentacaoValidator();
        AuditoriaProdutoService auditoriaProdutoService = new AuditoriaProdutoService();
        DashboardSnapshotScheduler scheduler = new DashboardSnapshotScheduler(produtoDAO);

        scheduler.iniciar(30);

        MovimentacaoService movimentacaoService = new MovimentacaoService
                                                                (movimentacaoDAO,
                                                                produtoDAO,
                                                                movimentacaoValidator,
                                                                ConnectionFactory.getDataSource());

        ProdutoValidator produtoValidator = new ProdutoValidator();

        ProdutoService produtoService = new ProdutoService(movimentacaoService, produtoDAO, produtoValidator);

        Menu menu = new Menu(produtoService, movimentacaoService, auditoriaProdutoService);

        menu.exibir();
        scheduler.parar();
    }
}