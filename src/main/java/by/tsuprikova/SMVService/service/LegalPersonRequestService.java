package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import org.springframework.http.ResponseEntity;

public interface LegalPersonRequestService {

    ResponseEntity<LegalPersonRequest> saveRequestForFine(LegalPersonRequest legalPersonRequest);
}
