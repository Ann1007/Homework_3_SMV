package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.ResponseWithFine;

public interface ResponseService {

    ResponseWithFine getResponseForFine(String sts);
    void deleteResponseWithFine(long id);


}
