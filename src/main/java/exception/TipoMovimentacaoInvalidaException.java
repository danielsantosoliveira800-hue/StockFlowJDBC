package exception;

public class TipoMovimentacaoInvalidaException extends RuntimeException{

    public TipoMovimentacaoInvalidaException(){
        super("Tipo de movimentação inválida.");
    }
}
