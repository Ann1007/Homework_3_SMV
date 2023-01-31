package by.tsuprikova.SMVService.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;


@Data
@Entity
@Table(name = "legal_person_request")
@RequiredArgsConstructor
public class LegalPersonRequest {
    @Id
    @org.hibernate.annotations.Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;
    private String sts;
    private Long INN;

}
