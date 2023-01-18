package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonRequestRepository extends JpaRepository<NaturalPersonRequest, Integer> {

    @Query(value = "SELECT MIN(id) FROM NATURAL_PERSON_REQUEST", nativeQuery = true)
    Integer findMinId();


}
