package main;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import db.ConnectionFactory;
import scheduler.DashboardSnapshotScheduler;
import service.AuditoriaProdutoService;
import service.MovimentacaoService;
import service.ProdutoService;
import ui.Menu;
import validation.MovimentacaoValidator;
import validation.ProdutoValidator;

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