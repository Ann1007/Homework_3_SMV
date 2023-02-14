package by.tsuprikova.SMVService.controller;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.LegalPersonRequestService;
import by.tsuprikova.SMVService.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@Slf4j
@RequestMapping("/smv/legal_person")
@RequiredArgsConstructor
public class SmvLegalPersonController {

    private final ResponseService responseService;
    private final LegalPersonRequestService legalPersonRequestService;


    @PostMapping("/save_request")
    public ResponseEntity<LegalPersonRequest> saveRequest(@Valid @RequestBody LegalPersonRequest legalPersonRequest) {

        return legalPersonRequestService.saveRequestForFine(legalPersonRequest);

    }


    @PostMapping("/get_response")
    public ResponseEntity<ResponseWithFine> getResponse(@Valid @RequestBody LegalPersonRequest legalPersonRequest) {

        return responseService.getResponseForFine(legalPersonRequest.getSts());

    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return responseService.deleteResponseWithFine(id);
    }

}
