package validation;

import model.Produto;

public class ProdutoValidation {

    public void validarProduto(Produto produto){

        if (produto.getStatus() == null) {
            throw new IllegalArgumentException("Status inválido.");
        }

        if (produto == null){
            throw new IllegalArgumentException("Produto inválido.");
        }

        if (produto.getNome() == null || produto.getNome().trim().isEmpty()){
            throw new IllegalArgumentException("Nome iválido.");
        }

        if (produto.getPreco() < 0 ){
            throw new IllegalArgumentException("Preço não pode ser menor que zero.");
        }

        if (produto.getQuantidade() < 0){
            throw new IllegalArgumentException("Quantidade não pode ser negativa.");

        }
    }

    public void atualizarPreco(double preco){

        if (preco < 0){
            throw new IllegalArgumentException("Preço inválido.");
        }
    }

    public void validarNome(String nome){

        if (nome == null || nome.trim().isEmpty()){
            throw new IllegalArgumentException("Nome inválido.");
        }
    }
}
