package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.LegalPersonRequest;

import java.util.UUID;

public interface LegalPersonRequestRepository {

    UUID findFirstId();

    LegalPersonRequest save(LegalPersonRequest legalPersonRequest);

    LegalPersonRequest getById(UUID id);

    void delete(UUID id);


}
