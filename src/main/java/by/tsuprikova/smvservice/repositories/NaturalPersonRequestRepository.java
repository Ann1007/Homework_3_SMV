package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.NaturalPersonRequest;

import java.util.UUID;


public interface NaturalPersonRequestRepository {

    UUID findFirstId() throws SmvServerException;

    NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest) throws SmvServerException;

    NaturalPersonRequest getById(UUID id) throws SmvServerException;

    int delete(UUID id) throws SmvServerException;


}
