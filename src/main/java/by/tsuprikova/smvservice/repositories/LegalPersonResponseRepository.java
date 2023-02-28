package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.LegalPersonResponse;


import java.util.UUID;

public interface LegalPersonResponseRepository {
    LegalPersonResponse findByINN(Long INN) throws SmvServiceException;

    void save(LegalPersonResponse response) throws SmvServiceException;

    int deleteById(UUID id) throws SmvServiceException;

}
