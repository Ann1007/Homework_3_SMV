package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonRequestRepository extends JpaRepository<NaturalPersonRequest,Long> {

    @Query(value = "SELECT MIN(id) FROM natural_person_request", nativeQuery = true)
    Long findMinId();


}
