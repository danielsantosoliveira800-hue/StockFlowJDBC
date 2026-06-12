package service;
import dao.ProdutoDAO;
import dao.ProdutoRepository;
import model.Movimentacao;
import model.Produto;
import model.ProdutoRanking;
import model.TipoMovimentacao;
import validation.ProdutoValidation;

import java.util.List;

public class ProdutoService {

    private final MovimentacaoService movimentacaoService;
    private final ProdutoValidation produtoValidation;
    private final ProdutoRepository produtoRepository;

    public ProdutoService() {
        this(new MovimentacaoService(), new ProdutoDAO(), new ProdutoValidation());
    }

    public ProdutoService(MovimentacaoService movimentacaoService,
                          ProdutoRepository produtoRepository,
                          ProdutoValidation produtoValidation)
    {
        this.movimentacaoService = movimentacaoService;
        this.produtoRepository = produtoRepository;
        this.produtoValidation = produtoValidation;
    }

    public void cadastrarProduto(Produto produto){
        produtoValidation.validarProduto(produto);
        produtoRepository.salvarProduto(produto);
    }

    public List<Produto> listar(){
        return produtoRepository.listar();
    }

    public void atualizarPreco(int id, double novoPreco){

        produtoValidation.atualizarPreco(novoPreco);

        Produto produto = buscarProduto(id);

        produtoRepository.atualizar(id,novoPreco);
    }

    public void deletar(int id) {

        Produto produto = buscarProduto(id);

        produtoRepository.deletar(id);
    }

    public Produto buscarPorID(int id) {
        return buscarProduto(id);
    }

    public List<Produto> buscarPorNome(String nome) {

        produtoValidation.validarNome(nome);

        return produtoRepository.buscarPorNome(nome);
    }

    public void entradaEstoque(int id, int quantidadeEntrada){


        Produto produto =  buscarProduto(id);

        validarQuantidade(quantidadeEntrada);

        registrarMovimentacao(produto.getId(), quantidadeEntrada, TipoMovimentacao.ENTRADA);

        }

    public void saidaEstoque(int id, int quantidadeSaida){

        Produto produto = buscarProduto(id);

        validarQuantidade(quantidadeSaida);

        registrarMovimentacao(produto.getId(), quantidadeSaida, TipoMovimentacao.SAIDA);
    }

    public List<Produto> buscarEstoqueBaixo(){
        return produtoRepository.buscarEstoqueBaixo();
    }

    public List<Produto> buscarEstoqueAtivo(){
        return produtoRepository.buscarProdutosAtivos();
    }

    public double calcularValorTotalEstoque(){
        return produtoRepository.calcularValorTotalEstoque();
    }

    public int contarProdutos(){
        return produtoRepository.contaProdutos();
    }

    public int contaProdutosAtivos(){
        return produtoRepository.contaProdutosAtivos();
    }

    public int contaProdutosInativos(){
       return produtoRepository.contaProdutosInativos();
    }

    public int somaQuantidadeProdutos(){
        return produtoRepository.quantidadeTotalProdutos();
    }

    public List<ProdutoRanking> buscarProdutoRanking(){
        return produtoRepository.buscarRankingProdutos();
    }

    private Produto buscarProduto(int id){

        if (id <= 0){
            throw new IllegalArgumentException("ID inválido.");
        }

        Produto produto = produtoRepository.buscar(id);

        if (produto == null){
            throw new IllegalArgumentException("Produto não encontrado.");
        }

        return produto;
    }

    private void registrarMovimentacao(int id, int quantidade, TipoMovimentacao tipo) {

        Movimentacao movimentacao = new Movimentacao();

        movimentacao.setProduto_id(id);
        movimentacao.setQuantidade(quantidade);
        movimentacao.setTipo(tipo);

        movimentacaoService.registrarMovimentacao(movimentacao);
    }

    private void validarQuantidade(int quantidade){

        if (quantidade <= 0){
            throw new IllegalArgumentException("Quantidade inválida.");
        }
    }
}
