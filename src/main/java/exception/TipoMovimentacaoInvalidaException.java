package exception;

public class TipoMovimentacaoInvalidaException extends AppException{

    public TipoMovimentacaoInvalidaException(){
        super("Tipo de movimentação inválida.");
    }
}
