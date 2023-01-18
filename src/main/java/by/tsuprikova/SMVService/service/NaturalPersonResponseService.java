package by.tsuprikova.SMVService.service;

import by.tsuprikova.SMVService.model.ResponseWithFine;

public interface NaturalPersonResponseService {

    ResponseWithFine getResponseForFine(String sts);
    void deleteResponseWithFine(int id);


}
