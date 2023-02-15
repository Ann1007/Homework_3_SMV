package by.tsuprikova.smvservice.service;

import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import org.springframework.http.ResponseEntity;

public interface NaturalPersonRequestService {

    ResponseEntity<NaturalPersonRequest> saveRequestForFine(NaturalPersonRequest naturalPersonRequest);
}
