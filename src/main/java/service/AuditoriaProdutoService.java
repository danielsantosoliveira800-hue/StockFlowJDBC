package service;

import infrasctructure.persistence.AuditoriaProdutosDAO;
import dao.AuditoriaProdutosRepository;
import domain.model.AuditoriaProdutos;

import java.util.List;

public class AuditoriaProdutoService {

    private final AuditoriaProdutosRepository auditoriaRepository;

    public AuditoriaProdutoService(){
        this(new AuditoriaProdutosDAO());
    }

    public AuditoriaProdutoService(AuditoriaProdutosRepository  auditoriaRepository){
        this.auditoriaRepository = auditoriaRepository;
    }

    public List<AuditoriaProdutos> listarAuditorias(){
        return auditoriaRepository.listar();
    }
}
