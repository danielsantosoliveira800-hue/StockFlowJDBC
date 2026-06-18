package model;

import java.time.LocalDateTime;

public class AuditoriaProdutos {

    private int id;
    private int produtoId;
    private String operacao;

    private String nomeAntigo;
    private String nomeNovo;

    private double precoAntigo;
    private double precoNovo;

    private int quantidadeAntiga;
    private int quantidadeNova;

    private String statusAntigo;
    private String statusNovo;

    private String usuarioBanco;
    private LocalDateTime dataAlteracao;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(int produtoId) {
        this.produtoId = produtoId;
    }

    public String getOperacao() {
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public String getNomeAntigo() {
        return nomeAntigo;
    }

    public void setNomeAntigo(String nomeAntigo) {
        this.nomeAntigo = nomeAntigo;
    }

    public String getNomeNovo() {
        return nomeNovo;
    }

    public void setNomeNovo(String nomeNovo) {
        this.nomeNovo = nomeNovo;
    }

    public double getPrecoAntigo() {
        return precoAntigo;
    }

    public void setPrecoAntigo(double precoAntigo) {
        this.precoAntigo = precoAntigo;
    }

    public double getPrecoNovo() {
        return precoNovo;
    }

    public void setPrecoNovo(double precoNovo) {
        this.precoNovo = precoNovo;
    }

    public int getQuantidadeAntiga() {
        return quantidadeAntiga;
    }

    public void setQuantidadeAntiga(int quantidadeAntiga) {
        this.quantidadeAntiga = quantidadeAntiga;
    }

    public int getQuantidadeNova() {
        return quantidadeNova;
    }

    public void setQuantidadeNova(int quantidadeNova) {
        this.quantidadeNova = quantidadeNova;
    }

    public String getStatusAntigo() {
        return statusAntigo;
    }

    public void setStatusAntigo(String statusAntigo) {
        this.statusAntigo = statusAntigo;
    }

    public String getStatusNovo() {
        return statusNovo;
    }

    public void setStatusNovo(String statusNovo) {
        this.statusNovo = statusNovo;
    }

    public String getUsuarioBanco() {
        return usuarioBanco;
    }

    public void setUsuarioBanco(String usuarioBanco) {
        this.usuarioBanco = usuarioBanco;
    }

    public LocalDateTime getDataAlteracao() {
        return dataAlteracao;
    }

    public void setDataAlteracao(LocalDateTime dataAlteracao) {
        this.dataAlteracao = dataAlteracao;
    }
}
