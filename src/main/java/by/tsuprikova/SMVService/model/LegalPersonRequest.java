package by.tsuprikova.SMVService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class LegalPersonRequest {

    private UUID id;
    @NotBlank(message = "поле стс не может быть пустое")
    private String sts;
    @Min(value = 1_000_000_000L, message = "поле ИНН должно состоять минимум из 10 цифр")
    private Long INN;

}
