package by.tsuprikova.smvservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

////localhost:9000/swagger-ui/index.html
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("SMV SERVICE")
                                .version("1.0.0")
                                .description("The smv service interacts with the adapter service, accepts the request, " +
                                        "then returns a response with a fine")
                                .contact(new Contact().name("Ann"))

                );
    }


}
