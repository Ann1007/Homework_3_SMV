package by.tsuprikova.smvservice.service;

import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface NaturalPersonResponseService {

    ResponseEntity<NaturalPersonResponse> getResponseForFine(String sts);

    ResponseEntity<Void> deleteResponseWithFine(UUID id);


}
