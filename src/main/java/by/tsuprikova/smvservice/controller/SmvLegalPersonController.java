package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.service.LegalPersonRequestService;
import by.tsuprikova.smvservice.service.LegalPersonResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequestMapping("/smv/legal_person")
@RequiredArgsConstructor
public class SmvLegalPersonController {

    private final LegalPersonResponseService naturalPersonResponseService;
    private final LegalPersonRequestService legalPersonRequestService;


    @PostMapping("/save_request")
    public ResponseEntity<LegalPersonRequest> saveRequest(@Valid @RequestBody LegalPersonRequest legalPersonRequest) {

        return legalPersonRequestService.saveRequestForFine(legalPersonRequest);

    }


    @PostMapping("/get_response")
    public ResponseEntity<LegalPersonResponse> getResponse(@Valid @RequestBody LegalPersonRequest legalPersonRequest) {

        return naturalPersonResponseService.getResponseByINN(legalPersonRequest.getInn());

    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return naturalPersonResponseService.deleteResponseWithFine(id);
    }

}
