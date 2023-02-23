package by.tsuprikova.smvservice.service;

import by.tsuprikova.smvservice.model.LegalPersonResponse;

import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface LegalPersonResponseService {

    ResponseEntity<LegalPersonResponse> getResponseByINN(Long INN);

    ResponseEntity<Void> deleteResponseWithFine(UUID id);
}
