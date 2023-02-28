package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;

import java.util.UUID;


public interface NaturalPersonResponseRepository {

    NaturalPersonResponse findBySts(String sts) throws SmvServiceException;

    void save(NaturalPersonResponse response) throws SmvServiceException;

    int deleteById(UUID id) throws SmvServiceException;


}
