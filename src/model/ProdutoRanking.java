package model;

public class ProdutoRanking {
    private String nomeProduto;
    private int totalMovimentacoes;
    private int quantidadeMovimentada;

    public ProdutoRanking() {
    }

    public ProdutoRanking(String nomeProduto, int totalMovimentacoes, int quantidadeMovimentada) {
        this.nomeProduto = nomeProduto;
        this.totalMovimentacoes = totalMovimentacoes;
        this.quantidadeMovimentada = quantidadeMovimentada;
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public int getTotalMovimentacoes() {
        return totalMovimentacoes;
    }

    public void setTotalMovimentacoes(int totalMovimentacoes) {
        this.totalMovimentacoes = totalMovimentacoes;
    }

    public int getQuantidadeMovimentada() {
        return quantidadeMovimentada;
    }

    public void setQuantidadeMovimentada(int quantidadeMovimentada) {
        this.quantidadeMovimentada = quantidadeMovimentada;
    }

    @Override
    public String toString() {
        return "ProdutoRanking{" +
                "nomePrtoduto='" + nomeProduto + '\'' +
                ", totalMovimentacoes=" + totalMovimentacoes +
                ", quantidadeMovimentada=" + quantidadeMovimentada +
                '}';
    }
}

