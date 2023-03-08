package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.service.NaturalPersonRequestService;
import by.tsuprikova.smvservice.service.NaturalPersonResponseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
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
@Tag(name = "Controller for natural person requests", description = "accepts a natural person request from adapter service,saves request, " +
        "returns a response with a fine then delete response")
@RequestMapping("api/v1/smv/natural_person")
public class SmvNaturalPersonController {

    private final NaturalPersonResponseService naturalPersonResponseService;
    private final NaturalPersonRequestService naturalPersonRequestService;


    @Operation(summary = "Save request from natural person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request was successfully saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NaturalPersonRequest.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = NaturalPersonRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with server", content = @Content)})

    @PostMapping("/request")
    public ResponseEntity<NaturalPersonRequest> saveRequest(@RequestBody(description = "natural person request", required = true,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NaturalPersonRequest.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = NaturalPersonRequest.class))})
                                                            @Valid @org.springframework.web.bind.annotation.RequestBody NaturalPersonRequest request) {

        return naturalPersonRequestService.saveRequestForFine(request);

    }


    @Operation(summary = "Get a response by request from natural person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = NaturalPersonResponse.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = NaturalPersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "The response is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with server", content = @Content)})

    @PostMapping("/response")
    public ResponseEntity<NaturalPersonResponse> getResponse(@RequestBody(description = "natural person request", required = true,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = NaturalPersonRequest.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = NaturalPersonRequest.class))})
                                                             @Valid @org.springframework.web.bind.annotation.RequestBody NaturalPersonRequest request) {

        return naturalPersonResponseService.getResponseForFine(request.getSts());


    }


    @Operation(summary = "delete response by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response was successfully deleted", content = @Content),
            @ApiResponse(responseCode = "405", description = "The response wasn't deleted by this id", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with server", content = @Content)})

    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return naturalPersonResponseService.deleteResponseWithFine(id);
    }


}

