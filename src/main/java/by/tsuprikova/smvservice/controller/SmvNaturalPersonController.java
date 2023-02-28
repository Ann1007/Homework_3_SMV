package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.service.NaturalPersonRequestService;
import by.tsuprikova.smvservice.service.NaturalPersonResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;


@RestController
@RequiredArgsConstructor
@Tag(name = "Controller for natural person requests", description = "accepts a request from natural person and returns a response with a fine")
@RequestMapping("api/v1/smv/natural_person")
public class SmvNaturalPersonController {

    private final NaturalPersonResponseService naturalPersonResponseService;
    private final NaturalPersonRequestService naturalPersonRequestService;


    @Operation(summary = "Save request from natural person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request was successfully saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NaturalPersonRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with DB", content = @Content)})

    @PostMapping("/request")
    public ResponseEntity<NaturalPersonRequest> saveRequest(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return naturalPersonRequestService.saveRequestForFine(naturalPersonRequest);

    }


    @Operation(summary = "Get a response by request from natural person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NaturalPersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "The response is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with DB", content = @Content)})

    @PostMapping("/response")
    public ResponseEntity<NaturalPersonResponse> getResponse(@Valid @RequestBody NaturalPersonRequest naturalPersonRequest) {

        return naturalPersonResponseService.getResponseForFine(naturalPersonRequest.getSts());


    }


    @Operation(summary = "delete response by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response was successfully deleted"),
            @ApiResponse(responseCode = "405", description = "The response wasn't deleted by this id", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with DB", content = @Content)})

    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return naturalPersonResponseService.deleteResponseWithFine(id);
    }


}

