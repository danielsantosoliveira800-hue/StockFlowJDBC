package model;

import java.util.List;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private String status;

    public Produto(){

    }

    public Produto(
                String nome,
                double preco,
                int quantidade,
                String status
    ) {
                this.id = id;
                this.nome = nome;
                this.preco = preco;
                this.quantidade = quantidade;
                this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
