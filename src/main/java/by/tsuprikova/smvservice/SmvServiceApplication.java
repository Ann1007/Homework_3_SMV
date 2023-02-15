package by.tsuprikova.smvservice;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
@RequiredArgsConstructor
public class SmvServiceApplication  {

    private final ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(SmvServiceApplication.class, args);


    }


}
