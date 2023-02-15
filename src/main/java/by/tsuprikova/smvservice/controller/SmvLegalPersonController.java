package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.ResponseWithFine;
import by.tsuprikova.smvservice.service.LegalPersonRequestService;
import by.tsuprikova.smvservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
