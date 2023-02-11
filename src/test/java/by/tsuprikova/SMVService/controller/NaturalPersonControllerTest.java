package by.tsuprikova.SMVService.controller;


import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.service.NaturalPersonRequestService;
import by.tsuprikova.SMVService.service.ResponseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class NaturalPersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NaturalPersonRequestService requestService;

    @MockBean
    private ResponseService responseService;

    private NaturalPersonRequest naturalPersonRequest;
    private ResponseWithFine responseWithFine;


    @BeforeEach
    void init() {

        naturalPersonRequest = new NaturalPersonRequest();
        UUID id = UUID.randomUUID();
        String sts = "59 ут 123456";
        naturalPersonRequest.setId(id);
        naturalPersonRequest.setSts(sts);

        responseWithFine = new ResponseWithFine();
        BigDecimal amountOfAccrual = new BigDecimal(28);
        BigDecimal amountOfPaid = new BigDecimal(28);
        int numberOfResolution = 321521;
        String articleOfKoap = "21.3";
        responseWithFine.setSts(sts);
        responseWithFine.setAmountOfAccrual(amountOfAccrual);
        responseWithFine.setArticleOfKoap(articleOfKoap);
        responseWithFine.setAmountOfPaid(amountOfPaid);
        responseWithFine.setId(UUID.randomUUID());
        responseWithFine.setNumberOfResolution(numberOfResolution);


    }


    @Test
    void SaveNaturalPersonRequestTest() throws Exception {

        Mockito.when(requestService.saveRequestForFine(any(NaturalPersonRequest.class))).thenReturn(naturalPersonRequest);

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value(naturalPersonRequest.getSts())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists()).
                andReturn();

    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {

        Mockito.when(responseService.getResponseForFine(any(String.class))).thenReturn(responseWithFine);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();


        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResponseWithFine resultResponseWithFine = objectMapper.readValue(resultContext, ResponseWithFine.class);

        Assertions.assertNotNull(resultResponseWithFine);
        Assertions.assertEquals("59 ут 123456", resultResponseWithFine.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfAccrual());
        Assertions.assertEquals(321521, resultResponseWithFine.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponseWithFine.getArticleOfKoap());


    }


    @Test
    void getResponseWithFineByStIsNull() throws Exception {

        Mockito.when(responseService.getResponseForFine(any(String.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.INTERNAL_SERVER_ERROR.value()));

    }


    @Test
    void deleteResponseWithFineById() throws Exception {

        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(responseService).deleteResponseWithFine(any(UUID.class));

        mockMvc.perform(MockMvcRequestBuilders.delete("/smv/natural_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

        Mockito.verify(responseService, Mockito.times(1)).deleteResponseWithFine(id);

    }


}
