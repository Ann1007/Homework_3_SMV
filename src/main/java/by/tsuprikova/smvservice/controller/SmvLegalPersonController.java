package by.tsuprikova.smvservice.controller;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.service.LegalPersonRequestService;
import by.tsuprikova.smvservice.service.LegalPersonResponseService;
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
@Tag(name = "Controller for legal person requests", description = "accepts a legal person request from adapter service,saves request, " +
        "returns a response with a fine then delete response")

@RequestMapping("api/v1/smv/legal_person")
public class SmvLegalPersonController {

    private final LegalPersonResponseService naturalPersonResponseService;
    private final LegalPersonRequestService legalPersonRequestService;

    @Operation(summary = "Save request from legal person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "The request was successfully saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LegalPersonRequest.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = LegalPersonRequest.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with server", content = @Content)})

    @PostMapping("/request")
    public ResponseEntity<LegalPersonRequest> saveRequest(@RequestBody(description = "legal person request", required = true,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LegalPersonRequest.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = LegalPersonRequest.class))})
                                                          @Valid @org.springframework.web.bind.annotation.RequestBody LegalPersonRequest request) {

        return legalPersonRequestService.saveRequestForFine(request);

    }


    @Operation(summary = "Get a response by request from legal person")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response is found",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = LegalPersonResponse.class)),
                            @Content(mediaType = "application/xml",
                                    schema = @Schema(implementation = LegalPersonResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "The response is not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "The problem with server", content = @Content)})

    @PostMapping("/response")
    public ResponseEntity<LegalPersonResponse> getResponse(@RequestBody(description = "legal person request", required = true,
            content = {@Content(mediaType = "application/json", schema = @Schema(implementation = LegalPersonRequest.class)),
                    @Content(mediaType = "application/xml", schema = @Schema(implementation = LegalPersonRequest.class))})
                                                           @Valid @org.springframework.web.bind.annotation.RequestBody LegalPersonRequest request) {

        return naturalPersonResponseService.getResponseByINN(request.getInn());

    }


    @Operation(summary = "delete legal person response by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The response was successfully deleted"),
            @ApiResponse(responseCode = "405", description = "The response wasn't deleted by this id"),
            @ApiResponse(responseCode = "500", description = "The problem with server")})

    @DeleteMapping("/response/{id}")
    public ResponseEntity<Void> deleteResponse(@PathVariable UUID id) {

        return naturalPersonResponseService.deleteResponseWithFine(id);
    }

}
