package by.tsuprikova.smvservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "LegalPersonRequest")
public class LegalPersonRequest {
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private UUID id;

    @Schema(description = "taxpayer identification number(ИНН - идентификационный номер налогоплательщика)")
    @Min(value = 1_000_000_000L, message = "the inn field must consist of at least 10 digits")
    private Long inn;

}
