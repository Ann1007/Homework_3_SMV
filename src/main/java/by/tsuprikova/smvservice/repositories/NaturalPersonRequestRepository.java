package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.NaturalPersonRequest;

import java.util.List;
import java.util.UUID;


public interface NaturalPersonRequestRepository {

    NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest) throws SmvServiceException;

    NaturalPersonRequest getById(UUID id) throws SmvServiceException;

    int delete(UUID id) throws SmvServiceException;

    List<NaturalPersonRequest> getAllRequests() throws SmvServiceException;


}
