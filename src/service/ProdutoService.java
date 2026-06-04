package service;
import dao.ProdutoDAO;
import model.Produto;
import model.ProdutoRanking;

import java.util.List;

public class ProdutoService {

    private ProdutoDAO produtoDao = new ProdutoDAO();

    public void cadastrarProduto(Produto produto){
        if (produto.getNome() == null
                || produto.getNome().trim().isEmpty() ){

            throw new IllegalArgumentException("Nome inválido");
        }

        if (produto.getPreco() < 0 ){

            throw new IllegalArgumentException("Preço não pode ser negativo.");
        }

        if (produto.getQuantidade() < 0){

            throw new IllegalArgumentException("Quantidade não pode ser negativa.");

        }

        produto.setStatus(produto.getStatus().trim().toUpperCase());

        produtoDao.salvarProduto(produto);
    }

    public List<Produto> listar(){
        return produtoDao.listar();
    }

    public void atualizarPreco(int id, double novoPreco){
        if (novoPreco < 0){
            throw new IllegalArgumentException("Preço inválido");
        }

        Produto produto = produtoDao.buscar(id);

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado.");
        }
        produtoDao.atualizar(id,novoPreco);
    }

    public void deletar(int id) {

        if (id <= 0){
            throw new IllegalArgumentException("ID inválido.");
        }

        Produto produto = produtoDao.buscar(id);

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado");
        }

        produtoDao.deletar(id);
    }

    public Produto buscarPorID(int id) {
        if (id <= 0){
            throw new IllegalArgumentException("ID inválido");
        }
        return produtoDao.buscar(id);
    }

    public List<Produto> buscarPorNome(String nome) {

        if (nome == null || nome .trim().isEmpty()){
            throw new IllegalArgumentException("Nome inválido.");
        }
        return produtoDao.buscarPorNome(nome);
    }

    public void entradaEstoque(int id, int quantidadeEntrada){
        if (quantidadeEntrada <= 0){
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        Produto produto =  produtoDao.buscar(id);

        if (produto == null ){
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        int novaQuantidade = produto.getQuantidade() + quantidadeEntrada;

        produtoDao.atualizarQuantidade(id, novaQuantidade);
    }

    public void saidaEstoque(int id, int quantidadeSaida){
        if (quantidadeSaida <=0 ){
            throw new IllegalArgumentException("Quantidade inválida");
        }

        Produto produto = produtoDao.buscar(id);

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        if (quantidadeSaida > produto.getQuantidade()){
            throw new IllegalArgumentException("Quantidade insuficiente no estoque.");
        }

        int novaQuantidade = produto.getQuantidade() - quantidadeSaida;

        produtoDao.atualizarQuantidade(id, novaQuantidade);
    }

    public List<Produto> buscarEstoqueBaixo(){
        return produtoDao.buscarEstoqueBaixo();
    }

    public List<Produto> buscarEstoqueAtivo(){
        return produtoDao.buscarProdutosAtivos();
    }

    public double calcularValorTotalEstoque(){
        return produtoDao.calcularValorTotalEstoque();
    }

    public int contarProdutos(){
        return produtoDao.contaProdutos();
    }

    public int contaProdutosAtivos(){
        return produtoDao.contaProdutosAtivos();
    }

    public int contaProdutosInativos(){
       return produtoDao.contaProdutosInativos();
    }

    public int somaQuantidadeProdutos(){
        return produtoDao.quantidadeTotalProdutos();
    }

    public List<ProdutoRanking> buscarProdutoRanking(){
        return produtoDao.buscarRankingProdutos();
    }
}
