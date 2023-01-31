package by.tsuprikova.SMVService.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "natural_person_fine_info")

public class InfoOfFineNaturalPerson {
    @Id
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private BigDecimal amountOfAccrual;
    private BigDecimal amountOfPaid;
    private int numberOfResolution;
    private String sts;
    private Date dateOfResolution;
    private String articleOfKoap;


}
