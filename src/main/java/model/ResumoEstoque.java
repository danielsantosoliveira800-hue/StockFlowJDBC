package model;

public class ResumoEstoque {

    private int totalProdutos;
    private int quantidadeTotalProdutos;
    private double valorTotalEstoque;
    private int produtosAtivos;
    private int produtosInativos;

    public int getTotalProdutos() {
        return totalProdutos;
    }

    public void setTotalProdutos(int totalProdutos) {
        this.totalProdutos = totalProdutos;
    }

    public int getQuantidadeTotalProdutos() {
        return quantidadeTotalProdutos;
    }

    public void setQuantidadeTotalProdutos(int quantidadeTotalProdutos) {
        this.quantidadeTotalProdutos = quantidadeTotalProdutos;
    }

    public double getValorTotalEstoque() {
        return valorTotalEstoque;
    }

    public void setValorTotalEstoque(double valorTotalEstoque) {
        this.valorTotalEstoque = valorTotalEstoque;
    }

    public int getProdutosAtivos() {
        return produtosAtivos;
    }

    public void setProdutosAtivos(int produtosAtivos) {
        this.produtosAtivos = produtosAtivos;
    }

    public int getProdutosInativos() {
        return produtosInativos;
    }

    public void setProdutosInativos(int produtosInativos) {
        this.produtosInativos = produtosInativos;
    }
}
