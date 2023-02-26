package by.tsuprikova.smvservice.worker;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@Slf4j
@RequiredArgsConstructor
public class WorkerInit {

    private final Worker worker;
    private ExecutorService executor = Executors.newSingleThreadExecutor();


    @PostConstruct
    public void start() {
        log.info("Worker is starting --------------------------");
        executor.execute(worker);

    }


    @PreDestroy
    public void finish() {
        executor.shutdown();
        log.info("Worker closed --------------------------");
    }

}
