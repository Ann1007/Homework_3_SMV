package by.tsuprikova.SMVService.controller;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.LegalPersonRequestService;
import by.tsuprikova.SMVService.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("/smv/legal_person")
@RequiredArgsConstructor
public class SmvLegalPersonController {

    private final ResponseService responseService;
    private final LegalPersonRequestService legalPersonRequestService;


    @PostMapping("/save_request")
    public LegalPersonRequest saveRequest(@RequestBody LegalPersonRequest legalPersonRequest) {

        log.info("save legal person request with sts '{}'", legalPersonRequest.getSts());
        LegalPersonRequest savedRequest = legalPersonRequestService.saveRequestForFine(legalPersonRequest);
        return savedRequest;

    }


    @PostMapping("/get_response")
    public ResponseEntity<ResponseWithFine> getResponse(@RequestBody LegalPersonRequest legalPersonRequest) {

        ResponseWithFine responseWithFine = responseService.getResponseForFine(legalPersonRequest.getSts());
        if (responseWithFine == null) {
            log.info("legal person response is null for sts '{}'", legalPersonRequest.getSts());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(responseWithFine, HttpStatus.OK);

    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        responseService.deleteResponseWithFine(id);
        log.info("delete legal person response with id= {}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
