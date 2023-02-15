package by.tsuprikova.smvservice.service;

import by.tsuprikova.smvservice.model.ResponseWithFine;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ResponseService {

    ResponseEntity<ResponseWithFine> getResponseForFine(String sts);
    ResponseEntity<Void> deleteResponseWithFine(UUID id);


}
