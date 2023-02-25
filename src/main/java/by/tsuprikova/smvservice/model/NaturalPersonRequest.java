package by.tsuprikova.smvservice.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonRequest {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "vehicle certificate (СТС - свидетельство транспортного средства)", example = "98 ут 253901")
    @NotBlank(message = "the sts field cannot be empty")
    private String sts;


}
