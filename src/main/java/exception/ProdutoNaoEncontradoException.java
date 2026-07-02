package exception;

public class ProdutoNaoEncontradoException extends AppException{

    public ProdutoNaoEncontradoException(){
        super("Produto não encontrado.");
    }
}
