package validation;

import exception.EstoqueInsuficienteException;
import exception.ProdutoNaoEncontradoException;
import exception.TipoMovimentacaoInvalidaException;
import exception.ValidacaoException;
import domain.model.Movimentacao;
import domain.model.Produto;
import domain.model.TipoMovimentacao;

public class MovimentacaoValidator {

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
            throw new ValidacaoException("Produto inválido.");
        }

        if (movimentacao.getQuantidade() <= 0){
            throw new ValidacaoException("Quantidade inválida.");
        }

        if (movimentacao.getTipo() == null){
            throw new ValidacaoException("Tipo de movimentação inválido.");
        }
    }
}
