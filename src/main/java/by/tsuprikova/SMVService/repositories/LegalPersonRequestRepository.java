package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface LegalPersonRequestRepository extends JpaRepository<LegalPersonRequest, Long> {

    @Query(value = "SELECT min(id) FROM legal_person_request", nativeQuery = true)
    Long findMinId();
}
