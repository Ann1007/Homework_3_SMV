package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.LegalPersonRequest;

import java.util.List;
import java.util.UUID;

public interface LegalPersonRequestRepository {

    LegalPersonRequest save(LegalPersonRequest legalPersonRequest) throws SmvServiceException;

    LegalPersonRequest getById(UUID id) throws SmvServiceException;

    int delete(UUID id) throws SmvServiceException;

    List<LegalPersonRequest> getAllRequests() throws SmvServiceException;


}
