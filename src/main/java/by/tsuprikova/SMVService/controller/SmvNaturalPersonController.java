package by.tsuprikova.SMVService.controller;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import by.tsuprikova.SMVService.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/smv/natural_person")
public class SmvNaturalPersonController {

    private final ResponseService responseService;
    private final NaturalPersonRequestService naturalPersonRequestService;


    @PostMapping("/save_request")
    public NaturalPersonRequest saveRequest(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        NaturalPersonRequest savedRequest = naturalPersonRequestService.saveRequestForFine(naturalPersonRequest);
        log.info("save natural person request was successfully saved with sts '{}', id={}", savedRequest.getSts(), savedRequest.getId());

        return savedRequest;

    }


    @PostMapping("/get_response")
    public ResponseEntity<ResponseWithFine> getResponse(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        ResponseWithFine responseWithFine = responseService.getResponseForFine(naturalPersonRequest.getSts());
        if (responseWithFine == null) {
            log.info("natural person response is null for sts '{}'", naturalPersonRequest.getSts());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);

        }

        return new ResponseEntity<>(responseWithFine, HttpStatus.OK);


    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        responseService.deleteResponseWithFine(id);
        log.info("natural person response was successfully deleted with id={}", id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

