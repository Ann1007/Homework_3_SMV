package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.InfoOfFineNaturalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NaturalPersonInfoRepository extends JpaRepository<InfoOfFineNaturalPerson, UUID> {

    InfoOfFineNaturalPerson findBySts(String sts);

}
