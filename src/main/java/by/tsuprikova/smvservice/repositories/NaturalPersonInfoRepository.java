package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.InfoOfFineNaturalPerson;


public interface NaturalPersonInfoRepository {

    InfoOfFineNaturalPerson findBySts(String sts) throws SmvServerException;

}
