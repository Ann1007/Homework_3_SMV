package by.tsuprikova.SMVService.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;


@Data
@Entity
@Table(name = "legal_person_request")
@RequiredArgsConstructor
public class LegalPersonRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sts;
    private Long INN;

}
