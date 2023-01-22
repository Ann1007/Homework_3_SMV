package by.tsuprikova.SMVService.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "natural_person_response")
public class ResponseWithFine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private BigDecimal amountOfAccrual;
    private BigDecimal amountOfPaid;
    private int numberOfResolution;
    private String sts;
    private Date dateOfResolution;
    private String articleOfKOAP;


}
