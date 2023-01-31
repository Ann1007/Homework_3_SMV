package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LegalPersonRequestRepository extends JpaRepository<LegalPersonRequest, UUID> {

    @Query(value = "SELECT id FROM legal_person_request limit 1", nativeQuery = true)
    UUID findFirstId();



}
