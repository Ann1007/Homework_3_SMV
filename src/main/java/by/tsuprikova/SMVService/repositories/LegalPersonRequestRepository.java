package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.LegalPersonRequest;

import java.util.UUID;

public interface LegalPersonRequestRepository {

    UUID findFirstId() throws SmvServerException;

    LegalPersonRequest save(LegalPersonRequest legalPersonRequest) throws SmvServerException;

    LegalPersonRequest getById(UUID id) throws SmvServerException;

    int delete(UUID id) throws SmvServerException;


}
