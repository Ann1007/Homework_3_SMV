package by.tsuprikova.SMVService.controller;


import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.LegalPersonRequestService;
import by.tsuprikova.SMVService.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class LegalPersonControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ResponseService responseService;

    @MockBean
    private LegalPersonRequestService legalPersonRequestService;

    private LegalPersonRequest request;
    private ResponseWithFine responseWithFine;

    @BeforeEach
    void init() {

        String sts = "13 са 111118";
        request = new LegalPersonRequest();
        request.setId(UUID.randomUUID());
        request.setSts(sts);
        request.setINN(2345676435L);

        responseWithFine = new ResponseWithFine();

        BigDecimal amountOfAccrual = new BigDecimal(44);
        BigDecimal amountOfPaid = new BigDecimal(44);
        int numberOfResolution = 1212;
        String articleOfKoap = "32.1";
        responseWithFine.setSts(sts);
        responseWithFine.setAmountOfAccrual(amountOfAccrual);
        responseWithFine.setArticleOfKoap(articleOfKoap);
        responseWithFine.setAmountOfPaid(amountOfPaid);
        responseWithFine.setId(UUID.randomUUID());
        responseWithFine.setNumberOfResolution(numberOfResolution);
    }


    @Test
    void saveLegalPersonRequest() throws Exception {

        Mockito.when(legalPersonRequestService.saveRequestForFine(any(LegalPersonRequest.class))).thenReturn(request);

        mockMvc.perform(post("/smv/legal_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.OK.value())).
                andExpect(jsonPath("$.id").exists()).
                andExpect(jsonPath("$.sts").value(request.getSts())).
                andExpect(jsonPath("$.inn").value(request.getINN()));
    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {

        Mockito.when(responseService.getResponseForFine(any(String.class))).thenReturn(responseWithFine);

        MvcResult result = mockMvc.perform(post("/smv/legal_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.OK.value())).
                andReturn();

        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseWithFine resultResponseWithFine = objectMapper.readValue(resultContext, ResponseWithFine.class);
        assertNotNull(resultResponseWithFine);
        assertEquals("13 са 111118", resultResponseWithFine.getSts());
        assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfAccrual());
        assertEquals(1212, resultResponseWithFine.getNumberOfResolution());
        assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfPaid());
        assertEquals("32.1", resultResponseWithFine.getArticleOfKoap());
    }


    @Test
    void getResponseWithFineByStIsNull() throws Exception {

        Mockito.when(responseService.getResponseForFine(any(String.class))).thenReturn(null);

        mockMvc.perform(post("/smv/legal_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }


    @Test
    void deleteResponseWithFineById() throws Exception {

        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(responseService).deleteResponseWithFine(any(UUID.class));

        mockMvc.perform(delete("/smv/legal_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.OK.value()));

        Mockito.verify(responseService, Mockito.times(1)).deleteResponseWithFine(id);

    }

}
