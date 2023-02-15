package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.ResponseWithFine;


import java.util.UUID;


public interface ResponseRepository {

    ResponseWithFine findBySts(String sts) throws SmvServerException;

    void save(ResponseWithFine response) throws SmvServerException;

    int deleteById(UUID id) throws SmvServerException;


}
