package by.tsuprikova.SMVService.worker;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
public class StartWorker {

    @Autowired
    private Worker worker;

    @PostConstruct
    public void startWorker() {
        log.info("Worker is starting --------------------------");
        worker.start();

    }

}
