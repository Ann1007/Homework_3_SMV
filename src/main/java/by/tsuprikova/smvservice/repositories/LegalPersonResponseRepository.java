package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.LegalPersonResponse;


import java.util.UUID;

public interface LegalPersonResponseRepository {
    LegalPersonResponse findByINN(Long INN) throws SmvServerException;

    void save(LegalPersonResponse response) throws SmvServerException;

    int deleteById(UUID id) throws SmvServerException;

}
