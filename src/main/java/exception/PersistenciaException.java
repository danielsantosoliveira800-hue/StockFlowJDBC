package exception;

public class PersistenciaException extends AppException{

    public PersistenciaException(String mensagem){
        super(mensagem);
    }

    public PersistenciaException(String mensagem, Throwable causa){
        super(mensagem, causa);
    }
}
