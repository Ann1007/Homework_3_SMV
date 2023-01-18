package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.ResponseWithFine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NaturalPersonResponseRepository extends JpaRepository<ResponseWithFine, Integer> {

    ResponseWithFine findBySts(String sts);
}
