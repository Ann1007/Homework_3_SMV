package by.tsuprikova.smvservice.service.impl;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.repositories.NaturalPersonResponseRepository;
import by.tsuprikova.smvservice.service.NaturalPersonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class NaturalPersonResponseServiceImpl implements NaturalPersonResponseService {

    private final NaturalPersonResponseRepository responseRepository;


    @Override
    public ResponseEntity<Void> deleteResponseWithFine(UUID id) {
        ResponseEntity<Void> responseEntity;
        try {
            int kol = responseRepository.deleteById(id);
            if (kol == 0) {
                log.info("natural person response can't be deleted with id={}", id);
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }

            responseEntity = new ResponseEntity<>(HttpStatus.OK);
            log.info("natural person response was successfully deleted with id={}", id);
        } catch (SmvServiceException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;

    }


    @Override
    public ResponseEntity<NaturalPersonResponse> getResponseForFine(String sts) {

        ResponseEntity<NaturalPersonResponse> response;
        try {
            NaturalPersonResponse responseWithFine = responseRepository.findBySts(sts);

            if (responseWithFine == null) {
                log.info("natural person response is null for sts '{}'", sts);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            log.info("natural person response was found for sts '{}'", sts);
            response = new ResponseEntity<>(responseWithFine, HttpStatus.OK);

        } catch (SmvServiceException e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }


}
