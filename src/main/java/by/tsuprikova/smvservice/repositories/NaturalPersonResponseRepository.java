package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;

import java.util.UUID;


public interface NaturalPersonResponseRepository {

    NaturalPersonResponse findBySts(String sts) throws SmvServerException;

    void save(NaturalPersonResponse response) throws SmvServerException;

    int deleteById(UUID id) throws SmvServerException;


}
