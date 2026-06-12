package validation;

import exception.EstoqueInsuficienteException;
import exception.ProdutoNaoEncontradoException;
import exception.TipoMovimentacaoInvalidaException;
import model.Movimentacao;
import model.Produto;
import model.TipoMovimentacao;

public class MovimentacaoValidation {

    public void validarProduto(Produto produto){


        if (produto == null){
            throw new ProdutoNaoEncontradoException();
        }

    }

    public void validarSaida(Produto produto, Movimentacao movimentacao){

        if (movimentacao .getTipo() == TipoMovimentacao.SAIDA &&
            movimentacao. getQuantidade() > produto.getQuantidade()){
            throw new EstoqueInsuficienteException();
        }
    }

    public void validarMovimentacao(Movimentacao movimentacao){

        if (movimentacao == null){
            throw new TipoMovimentacaoInvalidaException();
        }

        if (movimentacao.getProduto_id() <= 0){
            throw new IllegalArgumentException("Produto inválido.");
        }

        if (movimentacao.getQuantidade() <= 0){
            throw new IllegalArgumentException("Quantidade inválida.");
        }

        if (movimentacao.getTipo() == null){
            throw new IllegalArgumentException("Tipo de movimentação inválido.");
        }
    }
}
