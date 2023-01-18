package by.tsuprikova.SMVService.service.serviceImpl;

import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.repositories.NaturalPersonResponseRepository;
import by.tsuprikova.SMVService.service.NaturalPersonResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class NaturalPersonResponseServiceImpl implements NaturalPersonResponseService {

    private final NaturalPersonResponseRepository naturalPersonResponseRepository;


    @Override
    public void deleteResponseWithFine(int id) {
        naturalPersonResponseRepository.deleteById(id);
    }


    @Override
    public ResponseWithFine getResponseForFine(String sts) {

        ResponseWithFine responseWithFine = naturalPersonResponseRepository.findBySts(sts);

        return responseWithFine;
    }


}
