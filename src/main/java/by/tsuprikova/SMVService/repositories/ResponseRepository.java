package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.ResponseWithFine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ResponseRepository extends JpaRepository<ResponseWithFine, UUID> {

    ResponseWithFine findBySts(String sts);
}
