package by.tsuprikova.smvservice.model;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class NaturalPersonResponse extends ResponseWithFine {

    private String sts;

    @Builder(builderMethodName = "responseBuilder")
    public NaturalPersonResponse(UUID id, BigDecimal amountOfAccrual, BigDecimal amountOfPaid,
                                 int numberOfResolution, String sts, Date dateOfResolution, String articleOfKoap) {
        super(id, amountOfAccrual, amountOfPaid, numberOfResolution, dateOfResolution, articleOfKoap);
        this.sts = sts;
    }
}
