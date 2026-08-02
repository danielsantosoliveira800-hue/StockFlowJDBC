package service;

import infrastructure.persistence.ProdutoDAO;
import domain.model.*;
import domain.repository.ProdutoRepository;
import exception.ProdutoNaoEncontradoException;
import exception.ValidacaoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import domain.ProdutoValidator;

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
                          ProdutoValidator produtoValidator) {

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
        produtoRepository.atualizar(id, novoPreco);
    }

    public void desativar(int id){
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
        Produto produto = buscarProduto(id);
        produtoValidator.validarQuantidade(quantidadeEntrada);
        registrarMovimentacao(produto.getId(), quantidadeEntrada, TipoMovimentacao.ENTRADA);
    }

    public void saidaEstoque(int id, int quantidadeSaida){
        Produto produto = buscarProduto(id);
        produtoValidator.validarQuantidade(quantidadeSaida);
        registrarMovimentacao(produto.getId(), quantidadeSaida, TipoMovimentacao.SAIDA);
    }

    public void reativar(int id){
        Produto produto = buscarProduto(id);
        produtoRepository.reativar(produto.getId());
        auditLogger.info("Produto reativado: id={}, nome={}",
                produto.getId(), produto.getNome());
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
}