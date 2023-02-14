package by.tsuprikova.SMVService.service.impl;


import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.repositories.LegalPersonRequestRepository;
import by.tsuprikova.SMVService.service.LegalPersonRequestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class LegalPersonRequestServiceImpl implements LegalPersonRequestService {

    private final LegalPersonRequestRepository legalPersonRequestRepository;

    @Override
    public ResponseEntity<LegalPersonRequest> saveRequestForFine(LegalPersonRequest legalPersonRequest) {
        ResponseEntity<LegalPersonRequest> responseEntity;

        try {
            LegalPersonRequest savedRequest = legalPersonRequestRepository.save(legalPersonRequest);
            log.info("the legal person request was successfully saved with sts '{}', id={} ", savedRequest.getSts(), savedRequest.getId());
            responseEntity = new ResponseEntity<>(savedRequest, HttpStatus.ACCEPTED);
        } catch (SmvServerException e) {
            log.error(e.getMessage());
            responseEntity = new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return responseEntity;
    }
}

