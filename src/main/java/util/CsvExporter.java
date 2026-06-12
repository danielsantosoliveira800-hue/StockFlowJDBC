package util;

import model.ProdutoRanking;
import java.io.IOException;
import java.io.FileWriter;
import java.util.List;

public class CsvExporter {

    public void exportarRankingProdutos(List<ProdutoRanking> ranking ) {
        try (FileWriter writer = new FileWriter("ranking.csv")){

            writer.write("Produto;Movimentacoes;QuantidadeMovimentada\n");

            for (ProdutoRanking produtoRanking : ranking) {

                String linha =
                        produtoRanking.getNomeProduto() + ";" +
                                produtoRanking.getTotalMovimentacoes() + ";" +
                                produtoRanking.getQuantidadeMovimentada();

                writer.write(linha + "\n");
            }
            System.out.println("Arquivo CSV exportado com sucesso.");

        }catch (IOException e){
            throw new RuntimeException(e);
            }
        }
    }
