package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.service.NaturalPersonRequestService;
import by.tsuprikova.smvservice.service.NaturalPersonResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@RequestMapping("/smv/natural_person")
public class SmvNaturalPersonController {

    private final NaturalPersonResponseService naturalPersonResponseService;
    private final NaturalPersonRequestService naturalPersonRequestService;


    @PostMapping("/save_request")
    public ResponseEntity<NaturalPersonRequest> saveRequest(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return naturalPersonRequestService.saveRequestForFine(naturalPersonRequest);

    }


    @PostMapping("/get_response")
    public ResponseEntity<NaturalPersonResponse> getResponse(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return naturalPersonResponseService.getResponseForFine(naturalPersonRequest.getSts());


    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return naturalPersonResponseService.deleteResponseWithFine(id);
    }


}

