package domain.model;

public class Produto {
    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private StatusProduto status;
    private boolean desativadoManualmente;

    public Produto(){

    }

    public Produto(
            String nome,
            double preco,
            int quantidade,
            StatusProduto status
    ) {
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

    public StatusProduto getStatus() {
        return status;
    }

    public void setStatus(StatusProduto status) {
        this.status = status;
    }

    public boolean isDesativadoManualmente() {
        return desativadoManualmente;
    }

    public void setDesativadoManualmente(boolean desativadoManualmente) {
        this.desativadoManualmente = desativadoManualmente;
    }

}
