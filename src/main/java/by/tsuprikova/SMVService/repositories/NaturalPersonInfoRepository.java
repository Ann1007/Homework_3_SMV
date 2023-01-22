package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.InfoOfFineNaturalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NaturalPersonInfoRepository extends JpaRepository<InfoOfFineNaturalPerson, Long> {

    InfoOfFineNaturalPerson findBySts(String sts);

}
