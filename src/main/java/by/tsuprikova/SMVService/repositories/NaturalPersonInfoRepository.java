package by.tsuprikova.SMVService.repositories;

import by.tsuprikova.SMVService.model.InfoOfFineNaturalPerson;


public interface NaturalPersonInfoRepository {

    InfoOfFineNaturalPerson findBySts(String sts);

}