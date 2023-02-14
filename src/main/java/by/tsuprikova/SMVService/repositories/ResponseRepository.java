package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.ResponseWithFine;


import java.util.UUID;


public interface ResponseRepository {

    ResponseWithFine findBySts(String sts) throws SmvServerException;

    void save(ResponseWithFine response) throws SmvServerException;

    void deleteById(UUID id) throws SmvServerException;


}
