package by.tsuprikova.SMVService.service.impl;


import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class NaturalPersonRequestServiceImpl implements NaturalPersonRequestService {

    private final NaturalPersonRequestRepository naturalPersonRequestRepository;


    @Override
    public ResponseEntity<NaturalPersonRequest> saveRequestForFine(NaturalPersonRequest naturalPersonRequest) {
        ResponseEntity<NaturalPersonRequest> responseEntity;
        try {
            NaturalPersonRequest savedRequest = naturalPersonRequestRepository.save(naturalPersonRequest);
            log.info("the natural person request was successfully saved with sts '{}', id={} ", savedRequest.getSts(), savedRequest.getId());
            responseEntity = new ResponseEntity<>(savedRequest, HttpStatus.ACCEPTED);
        } catch (SmvServerException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}
