package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.ResponseWithFine;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ResponseService {

    ResponseEntity<ResponseWithFine> getResponseForFine(String sts);
    ResponseEntity<Void> deleteResponseWithFine(UUID id);


}
