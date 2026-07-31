package validation;

import exception.ValidacaoException;
import domain.model.Produto;

public class ProdutoValidator {

    public void validarProduto(Produto produto) {

        if (produto == null) {
            throw new ValidacaoException("Produto inválido.");
        }

        if (produto.getStatus() == null) {
            throw new ValidacaoException("Status inválido.");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new ValidacaoException("Nome iválido.");
        }

        if (produto.getPreco() < 0) {
            throw new ValidacaoException("Preço não pode ser menor que zero.");
        }

        if (produto.getQuantidade() < 0) {
            throw new ValidacaoException("Quantidade não pode ser negativa.");

        }
    }

    public void atualizarPreco(double preco) {

        if (preco < 0) {
            throw new ValidacaoException("Preço inválido.");
        }
    }

    public void validarNome(String nome) {

        if (nome == null || nome.trim().isEmpty()) {
            throw new ValidacaoException("Nome inválido.");
        }
    }

    public void validarQuantidade(int quantidade) {
        if (quantidade <= 0) {
            throw new ValidacaoException("Quantidade inválida.");

        }
    }
}