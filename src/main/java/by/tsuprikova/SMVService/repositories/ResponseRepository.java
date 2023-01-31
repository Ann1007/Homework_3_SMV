package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.ResponseWithFine;


import java.util.UUID;


public interface ResponseRepository {

    ResponseWithFine findBySts(String sts);

    void save(ResponseWithFine response);

    void deleteById(UUID id);


}
