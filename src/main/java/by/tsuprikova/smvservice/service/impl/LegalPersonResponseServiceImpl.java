package by.tsuprikova.smvservice.service.impl;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.repositories.LegalPersonResponseRepository;
import by.tsuprikova.smvservice.service.LegalPersonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class LegalPersonResponseServiceImpl implements LegalPersonResponseService {

    private final LegalPersonResponseRepository responseRepository;


    @Override
    public ResponseEntity<LegalPersonResponse> getResponseByINN(Long INN) {

        ResponseEntity<LegalPersonResponse> response;
        try {
            LegalPersonResponse responseWithFine = responseRepository.findByINN(INN);
            if (responseWithFine == null) {
                log.info("legal person response is null for INN '{}'", INN);
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            log.info("legal person response was found for INN '{}'", INN);
            response = new ResponseEntity<>(responseWithFine, HttpStatus.OK);

        } catch (SmvServiceException e) {
            log.error(e.getMessage());
            response = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response;
    }



    @Override
    public ResponseEntity<Void> deleteResponseWithFine(UUID id) {

        ResponseEntity<Void> responseEntity;
        try {
            int kol = responseRepository.deleteById(id);
            if (kol == 0) {
                log.info("legal person response can't be deleted with id={}", id);
                return new ResponseEntity<>(HttpStatus.METHOD_NOT_ALLOWED);
            }

            responseEntity = new ResponseEntity<>(HttpStatus.OK);
            log.info("legal person response was successfully deleted with id={}", id);
        } catch (SmvServiceException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
