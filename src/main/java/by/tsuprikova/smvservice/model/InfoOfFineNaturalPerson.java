package by.tsuprikova.smvservice.model;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoOfFineNaturalPerson {

    private UUID id;

    @Schema(description = "amount of accrual (сумма по начислению)")
    private BigDecimal amountOfAccrual;

    @Schema(description = "amount of paid(сумма к оплате)")
    private BigDecimal amountOfPaid;

    @Schema(description = "number of resolution(номер постановления)")
    private int numberOfResolution;

    @Schema(description = "vehicle certificate (СТС - свидетельство транспортного средства)", example = "98 ут 253901")
    private String sts;

    @Schema(description = "date of resolution(дата постановления)")
    private Date dateOfResolution;

    @Schema(description = "article of Koap(статья КоАП)")
    private String articleOfKoap;


}
