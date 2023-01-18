package by.tsuprikova.SMVService.controller;

import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import by.tsuprikova.SMVService.service.NaturalPersonResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/smv/natural_person")
public class SmvNaturalPersonController {

    private final NaturalPersonResponseService naturalPersonResponseService;
    private final NaturalPersonRequestService naturalPersonRequestService;


    @PostMapping("/save_request")
    public ResponseEntity<Void> saveRequest(@RequestBody NaturalPersonRequest naturalPersonRequest) {
        log.info("save natural person request with sts '{}'", naturalPersonRequest.getSts());
        naturalPersonRequestService.saveRequestForFine(naturalPersonRequest);
        return new ResponseEntity<>(HttpStatus.CONTINUE);

    }


    @PostMapping("/get_response")
    public ResponseEntity<ResponseWithFine> getResponse(@RequestBody NaturalPersonRequest naturalPersonRequest) {

        ResponseWithFine responseWithFine = naturalPersonResponseService.getResponseForFine(naturalPersonRequest.getSts());
        if (responseWithFine == null) {
            log.info("natural person response is null for sts '{}'", naturalPersonRequest.getSts());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>(responseWithFine, HttpStatus.OK);


    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable int id) {

        naturalPersonResponseService.deleteResponseWithFine(id);
        log.info("delete natural person response with id= {}",id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}

