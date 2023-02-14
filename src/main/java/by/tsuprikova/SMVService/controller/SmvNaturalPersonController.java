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

