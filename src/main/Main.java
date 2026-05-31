package main;

import dao.ProdutoDAO;
import db.ConnectionFactory;
import model.Produto;
import ui.Menu;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        Menu menu = new Menu();
        menu.exibir();
    }
}