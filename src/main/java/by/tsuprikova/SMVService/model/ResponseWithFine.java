package by.tsuprikova.SMVService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "response_with_fine")
public class ResponseWithFine {
    @Id
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private BigDecimal amountOfAccrual;
    private BigDecimal amountOfPaid;
    private int numberOfResolution;
    private String sts;
    private Date dateOfResolution;
    private String articleOfKoap;

    public ResponseWithFine(BigDecimal amountOfAccrual, BigDecimal amountOfPaid, int numberOfResolution, String sts, Date dateOfResolution, String articleOfKoap) {
        this.amountOfAccrual = amountOfAccrual;
        this.amountOfPaid = amountOfPaid;
        this.numberOfResolution = numberOfResolution;
        this.sts = sts;
        this.dateOfResolution = dateOfResolution;
        this.articleOfKoap = articleOfKoap;
    }
}
