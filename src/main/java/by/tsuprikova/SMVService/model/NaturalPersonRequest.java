package by.tsuprikova.SMVService.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "natural_person_request")
public class NaturalPersonRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String sts;


}
