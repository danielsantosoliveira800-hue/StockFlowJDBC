package service;
import dao.ProdutoDAO;
import dao.ProdutoRepository;
import domain.model.*;
import exception.ProdutoNaoEncontradoException;
import exception.ValidacaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import domain.ProdutoValidator;

import java.util.ArrayList;
import java.util.List;

public class ProdutoService {

    private final MovimentacaoService movimentacaoService;
    private final ProdutoValidator produtoValidator;
    private final ProdutoRepository produtoRepository;
    private static final Logger auditLogger = LoggerFactory.getLogger("AUDIT");

    public ProdutoService() {
        this(new MovimentacaoService(), new ProdutoDAO(), new ProdutoValidator());
    }

    public ProdutoService(MovimentacaoService movimentacaoService,
                          ProdutoRepository produtoRepository,
                          ProdutoValidator produtoValidator)
    {
        this.movimentacaoService = movimentacaoService;
        this.produtoRepository = produtoRepository;
        this.produtoValidator = produtoValidator;
    }

    public void cadastrarProduto(Produto produto){
        produtoValidator.validarProduto(produto);

        StatusProduto statusInicial =
                produto.getQuantidade() == 0 ? StatusProduto.INATIVO : StatusProduto.ATIVO;

        produto.setStatus(statusInicial);

        produtoRepository.salvarProduto(produto);

        auditLogger.info("Produto cadastrado: nome={}, preco={}, quantidade={}, status={}",
                produto.getNome(), produto.getPreco(), produto.getQuantidade(), produto.getStatus());
    }

    public List<Produto> listar(){
        return produtoRepository.listar();
    }

    public void atualizarPreco(int id, double novoPreco){

        produtoValidator.atualizarPreco(novoPreco);

        Produto produto = buscarProduto(id);

        produtoRepository.atualizar(id,novoPreco);
    }

    public void desativar(int id)   {

        Produto produto = buscarProduto(id);

        produtoRepository.desativar(produto.getId());

        auditLogger.info("Produto desativado manualmente: id={}, nome={}",
                produto.getId(), produto.getNome());
    }

    public Produto buscarPorID(int id) {
        return buscarProduto(id);
    }

    public List<Produto> buscarPorNome(String nome) {

        produtoValidator.validarNome(nome);

        return produtoRepository.buscarPorNome(nome);
    }

    public void entradaEstoque(int id, int quantidadeEntrada){


        Produto produto =  buscarProduto(id);

        produtoValidator.validarQuantidade(quantidadeEntrada);

        registrarMovimentacao(produto.getId(), quantidadeEntrada, TipoMovimentacao.ENTRADA);

        }

    public void saidaEstoque(int id, int quantidadeSaida){

        Produto produto = buscarProduto(id);

        produtoValidator.validarQuantidade(quantidadeSaida);

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
            throw new ValidacaoException("ID inválido.");
        }

        Produto produto = produtoRepository.buscar(id);

        if (produto == null){
            throw new ProdutoNaoEncontradoException();
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


    public ResumoEstoque buscarResumoEstoque(){
        return produtoRepository.buscarResumoEstoque();
    }

    public double calcularValorProduto(int id){

        buscarProduto(id);

        return produtoRepository.calcularValorProduto(id);
    }

    public void inserirProdutosEmLote(int quantidade){

        List<Produto> produtos =  new ArrayList<>();

        for (int i = 0; i < quantidade; i++) {
            produtos.add(
                    new Produto(
                            "Produto teste "+ i,
                            10.0 + i,
                            100,
                            StatusProduto.ATIVO
                    )
            );
        }
        produtoRepository.inserirProdutoEmLote(produtos);
    }

    public void inserirProdutosComSavepoint(List<Produto> produtos){
        produtoRepository.inserirProdutosComSavepoint(produtos);
    }

    public void testarSavepoint(){

        List<Produto> produtos = new ArrayList<>();

        for (int i = 1; i <= 5 ; i++) {
            produtos.add(
                    new Produto(
                            "produto savepoint "+ i,
                            100 + i,
                            10,
                            StatusProduto.ATIVO
                    )
            );
        }
        inserirProdutosComSavepoint(produtos);
    }

    public void reativar(int id){
        Produto produto =  buscarProduto(id);
        produtoRepository.reativar(produto.getId());
        auditLogger.info("Produto reativado: id={}, nome={}",
                produto.getId(), produto.getNome());
    }
}
