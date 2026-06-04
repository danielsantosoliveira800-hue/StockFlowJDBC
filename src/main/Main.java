package main;

import dao.MovimentacaoDAO;
import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Movimentacao;
import model.Produto;
import ui.Menu;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.exibir();

        /*MovimentacaoDAO dao =
                new MovimentacaoDAO();

        Movimentacao movimentacao =
                new Movimentacao(2, "ENTRADA", 10);

        dao.registrarMovimentacao(movimentacao);

        System.out.println("Movimentação registrada.");*/
    }
}