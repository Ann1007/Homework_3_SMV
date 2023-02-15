package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.ResponseWithFine;
import by.tsuprikova.smvservice.service.NaturalPersonRequestService;
import by.tsuprikova.smvservice.service.ResponseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public ResponseEntity<NaturalPersonRequest> saveRequest(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return naturalPersonRequestService.saveRequestForFine(naturalPersonRequest);

    }


    @PostMapping("/get_response")
    public ResponseEntity<ResponseWithFine> getResponse(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return responseService.getResponseForFine(naturalPersonRequest.getSts());


    }


    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return responseService.deleteResponseWithFine(id);
    }


}

