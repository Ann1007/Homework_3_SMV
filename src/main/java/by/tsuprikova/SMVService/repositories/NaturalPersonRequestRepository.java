package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;

import java.util.UUID;


public interface NaturalPersonRequestRepository {

    UUID findFirstId() throws SmvServerException;

    NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest) throws SmvServerException;

    NaturalPersonRequest getById(UUID id) throws SmvServerException;

    void delete(UUID id) throws SmvServerException;


}
