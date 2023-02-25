package by.tsuprikova.smvservice.worker;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
@Slf4j
public class StartWorker {

    @Autowired
    private Worker worker;

    @PostConstruct
    public void startWorker() {
        log.info("Worker is starting --------------------------");
        worker.setName("Worker");
        worker.start();

    }

    @PreDestroy
    public void finish() {
        Worker.isStopped=true;
        log.info("Worker closed --------------------------");
    }

}
