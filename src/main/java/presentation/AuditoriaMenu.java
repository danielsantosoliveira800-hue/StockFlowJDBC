package presentation;

import domain.model.AuditoriaProdutos;
import service.AuditoriaProdutoService;

import java.util.List;

public class AuditoriaMenu {

    private final AuditoriaProdutoService auditoriaProdutoService;

    public AuditoriaMenu(AuditoriaProdutoService auditoriaProdutoService) {
        this.auditoriaProdutoService = auditoriaProdutoService;
    }

    public void exibir() {
        List<AuditoriaProdutos> auditorias = auditoriaProdutoService.listarAuditorias();

        if (auditorias.isEmpty()){
            System.out.println("Nenhuma auditoria encontrada.");
            return;
        }

        for (AuditoriaProdutos auditoria : auditorias) {
            System.out.println("---------------------------------------------");
            System.out.println("Operção: "+ auditoria.getOperacao());
            System.out.println("Produto id: "+ auditoria.getProdutoId());
            System.out.println("Nome antigo: "+ auditoria.getNomeAntigo());
            System.out.println("Nome novo: "+ auditoria.getNomeNovo());
            System.out.println("Preço antigo: R$ "+ auditoria.getPrecoAntigo());
            System.out.println("Preço novo: R$ "+ auditoria.getPrecoNovo());
            System.out.println("Quantidade antiga: "+ auditoria.getQuantidadeAntiga());
            System.out.println("Quantidade novo: "+ auditoria.getQuantidadeNova());
            System.out.println("Status antigo: "+ auditoria.getStatusAntigo());
            System.out.println("Status novo: "+ auditoria.getStatusNovo());
            System.out.println("Usuário banco: "+ auditoria.getUsuarioBanco());
            System.out.println("Data da alterção: "+ auditoria.getDataAlteracao());
        }
    }
}