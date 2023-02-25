package by.tsuprikova.smvservice.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


@Data
@RequiredArgsConstructor
public class LegalPersonResponse extends ResponseWithFine {

    @Schema(description = "taxpayer identification number(ИНН - идентификационный номер налогоплательщика)")
    private Long inn;

    @Builder(builderMethodName = "LegalResponseBuilder")
    public LegalPersonResponse(UUID id, BigDecimal amountOfAccrual, BigDecimal amountOfPaid,
                               int numberOfResolution, Long inn, Date dateOfResolution, String articleOfKoap) {
        super(id, amountOfAccrual, amountOfPaid, numberOfResolution, dateOfResolution, articleOfKoap);
        this.inn = inn;
    }
}
