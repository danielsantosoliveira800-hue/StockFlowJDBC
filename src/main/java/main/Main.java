package main;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Movimentacao;
import model.Produto;
import service.MovimentacaoService;
import service.ProdutoService;
import ui.Menu;
import validation.MovimentacaoValidation;
import validation.ProdutoValidation;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        ProdutoDAO produtoDAO = new ProdutoDAO();
        MovimentacaoDAO movimentacaoDAO = new MovimentacaoDAO();
        MovimentacaoValidation movimentacaoValidation =  new MovimentacaoValidation();

        MovimentacaoService movimentacaoService = new MovimentacaoService
                                                        (movimentacaoDAO,
                                                        produtoDAO,
                                                        movimentacaoValidation,
                                                        ConnectionFactory.getDataSource());

        ProdutoValidation produtoValidation = new ProdutoValidation();

        ProdutoService produtoService = new ProdutoService(movimentacaoService, produtoDAO, produtoValidation);

        Menu menu = new Menu(produtoService, movimentacaoService);

        menu.exibir();
    }
}