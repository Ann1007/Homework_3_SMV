package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.NaturalPersonRequest;

import java.util.UUID;


public interface NaturalPersonRequestRepository {

    UUID findRandomId() throws SmvServiceException;

    NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest) throws SmvServiceException;

    NaturalPersonRequest getById(UUID id) throws SmvServiceException;

    int delete(UUID id) throws SmvServiceException;


}
