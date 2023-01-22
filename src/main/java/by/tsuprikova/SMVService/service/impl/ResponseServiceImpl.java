package by.tsuprikova.SMVService.service.impl;

import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.repositories.ResponseRepository;
import by.tsuprikova.SMVService.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ResponseServiceImpl implements ResponseService {

    private final ResponseRepository responseRepository;


    @Override
    public void deleteResponseWithFine(long id) {
        responseRepository.deleteById(id);
    }


    @Override
    public ResponseWithFine getResponseForFine(String sts) {

        ResponseWithFine responseWithFine = responseRepository.findBySts(sts);

        return responseWithFine;
    }


}
