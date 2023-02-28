package by.tsuprikova.smvservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class ExampleAOP {


    @Pointcut("execution(public void by.tsuprikova.smvservice.worker.Worker.run())")
    public void callAfterWorkerStarted() {
    }

    @Before("callAfterWorkerStarted()")
    public void afterWorkerStart() {
        log.info("Worker is working(aop)....");
    }
}
