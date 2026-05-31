package Service;
import dao.ProdutoDAO;
import model.Produto;

import java.util.List;

public class ProdutoService {

    private ProdutoDAO dao = new ProdutoDAO();

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

        dao.salvarProduto(produto);
    }

    public List<Produto> listar(){
        return dao.listar();
    }

    public void atualizarPreco(int id, double novoPreco){
        if (novoPreco < 0){
            throw new IllegalArgumentException("Preço inválido");
        }

        dao.atualizar(id,novoPreco);
    }

    public void deletar(int id) {

        if (id <= 0){
            throw new IllegalArgumentException("ID inválido.");
        }

        dao.deletar(id);
    }

    public Produto buscarPorID(int id) {
        if (id <= 0){
            throw new IllegalArgumentException("ID inválido");
        }
        return dao.buscar(id);
    }

    public List<Produto> buscarPorNome(String nome) {

        if (nome == null || nome .trim().isEmpty()){
            throw new IllegalArgumentException("Nomw inválido.");
        }
        return dao.buscarPorNome(nome);
    }

    public void entradaEstoque(int id, int quantidadeEntrada){
        if (quantidadeEntrada <= 0){
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        Produto produto =  dao.buscar(id);

        if (produto == null ){
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        int novaQuantidade = produto.getQuantidade() + quantidadeEntrada;

        dao.atualizarQuantidade(id, novaQuantidade);
    }

    public void saidaEstoque(int id, int quantidadeSaida){
        if (quantidadeSaida <=0 ){
            throw new IllegalArgumentException("Quantidade inválida");
        }

        Produto produto = dao.buscar(id);

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        if (quantidadeSaida > produto.getQuantidade()){
            throw new IllegalArgumentException("Quantidade insuficiente no estoque.");
        }

        int novaQuantidade = produto.getQuantidade() - quantidadeSaida;

        dao.atualizarQuantidade(id, novaQuantidade);
    }

    public List<Produto> buscarEstoqueBaixo(){
        return dao.buscarEstoqueBaixo();
    }

    public List<Produto> buscarEstoqueAtivo(){
        return dao.buscarProdutosAtivos();
    }

    public double calcularValorTotalEstoque(){
        return dao.calcularValorTotalEstoque();
    }

    public int contarProdutos(){
        return dao.contaProdutos();
    }

    public int contaProdutosAtivos(){
        return dao.contaProdutosAtivos();
    }

    public int contaProdutosInativos(){
       return dao.contaProdutosInativos();
    }

    public int somaQuantidadeProdutos(){
        return dao.qunatidadeTotalProdutos();
    }
}
