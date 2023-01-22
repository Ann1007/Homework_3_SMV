package by.tsuprikova.SMVService.model;


import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@Entity
@Table(name = "fine_info")

public class InfoOfFineNaturalPerson {
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
