package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.LegalPersonRequest;

import java.util.UUID;

public interface LegalPersonRequestRepository {

    UUID findFirstId() throws SmvServerException;

    LegalPersonRequest save(LegalPersonRequest legalPersonRequest) throws SmvServerException;

    LegalPersonRequest getById(UUID id) throws SmvServerException;

    int delete(UUID id) throws SmvServerException;


}
