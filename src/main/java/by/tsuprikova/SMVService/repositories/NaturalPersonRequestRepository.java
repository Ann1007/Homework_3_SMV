package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;

import java.util.UUID;


public interface NaturalPersonRequestRepository {

    UUID findFirstId();

    NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest);

    NaturalPersonRequest getById(UUID id);

    void delete(UUID id);


}
