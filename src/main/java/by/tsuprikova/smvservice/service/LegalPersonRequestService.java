package by.tsuprikova.smvservice.service;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import org.springframework.http.ResponseEntity;

public interface LegalPersonRequestService {

    ResponseEntity<LegalPersonRequest> saveRequestForFine(LegalPersonRequest legalPersonRequest);
}
