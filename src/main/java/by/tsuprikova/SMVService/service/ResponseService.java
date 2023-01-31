package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.ResponseWithFine;

import java.util.UUID;

public interface ResponseService {

    ResponseWithFine getResponseForFine(String sts);
    void deleteResponseWithFine(UUID id);


}
