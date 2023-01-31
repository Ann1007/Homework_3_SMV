package by.tsuprikova.SMVService.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NaturalPersonRequest {

    private UUID id;
    private String sts;


}
