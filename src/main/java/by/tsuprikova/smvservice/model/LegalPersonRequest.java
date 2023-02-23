package by.tsuprikova.smvservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.Min;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalPersonRequest {

    private UUID id;
    @Min(value = 1_000_000_000L, message = "поле ИНН должно состоять минимум из 10 цифр")
    private Long inn;

}
