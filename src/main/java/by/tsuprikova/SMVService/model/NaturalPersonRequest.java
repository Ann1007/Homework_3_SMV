package by.tsuprikova.SMVService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonRequest {

    private UUID id;
    @NotBlank(message = "поле стс не может быть пустое")
    private String sts;


}
