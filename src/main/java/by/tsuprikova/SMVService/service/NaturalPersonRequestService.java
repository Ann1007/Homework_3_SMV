package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import org.springframework.http.ResponseEntity;

public interface NaturalPersonRequestService {

    ResponseEntity<NaturalPersonRequest> saveRequestForFine(NaturalPersonRequest naturalPersonRequest);
}
