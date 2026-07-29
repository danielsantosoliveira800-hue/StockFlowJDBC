package scheduler;

import dao.ProdutoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DashboardSnapshotScheduler {

    private static final Logger logger = LoggerFactory.getLogger(DashboardSnapshotScheduler.class);

    private final ProdutoRepository produtoRepository;
    private final ScheduledExecutorService executor;

    public DashboardSnapshotScheduler(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
        this.executor = Executors.newSingleThreadScheduledExecutor();
    }

    public void iniciar(long intervaloEmMinutos){
        executor.scheduleAtFixedRate(()-> {
            try {
                produtoRepository.gerarSnapshotDasDashboard();
            } catch (Exception e) {
                logger.error("Erro ao gerar snapshot agendado do dashboard.", e);
            }
        }, 0, intervaloEmMinutos, TimeUnit.MINUTES);

        logger.info("Agendador de snapshot do dashboard iniciado (intervalo: {} min).", intervaloEmMinutos);
    }

    public void parar(){
        executor.shutdown();
        logger.info("Agendador de snapshot do dashboard finalizado.");
    }
}