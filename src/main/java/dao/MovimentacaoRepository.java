package dao;

import domain.model.Movimentacao;

import java.util.List;
import java.sql.Connection;
import java.time.LocalDate;

public interface MovimentacaoRepository {

    void registrarMovimentacao(Connection connection, Movimentacao movimentacao);

    List<Movimentacao> listar();

    List<Movimentacao> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim);

}
