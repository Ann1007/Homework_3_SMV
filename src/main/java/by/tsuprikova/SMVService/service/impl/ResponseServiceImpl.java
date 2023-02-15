package by.tsuprikova.SMVService.service.impl;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.repositories.ResponseRepository;
import by.tsuprikova.SMVService.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class ResponseServiceImpl implements ResponseService {

    private final ResponseRepository responseRepository;


    @Override
    public ResponseEntity<Void> deleteResponseWithFine(UUID id) {
        ResponseEntity<Void> responseEntity;
        try {
            int kol = responseRepository.deleteById(id);
            if (kol == 0) {
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }

            responseEntity = new ResponseEntity<>(HttpStatus.OK);
            log.info("response was successfully deleted with id={}", id);
        } catch (SmvServerException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }


    @Override
    public ResponseEntity<ResponseWithFine> getResponseForFine(String sts) {

        ResponseEntity<ResponseWithFine> response;
        try {
            ResponseWithFine responseWithFine = responseRepository.findBySts(sts);

            if (responseWithFine == null) {
                log.info("response is null for sts '{}'", sts);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            response = new ResponseEntity<>(responseWithFine, HttpStatus.OK);

        } catch (SmvServerException e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }


}
