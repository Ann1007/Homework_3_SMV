package by.tsuprikova.SMVService.model;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "natural_person_request")
public class NaturalPersonRequest {
    @Id
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String sts;


}
