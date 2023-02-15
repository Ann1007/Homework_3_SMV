package by.tsuprikova.smvservice.controller;


import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.ResponseWithFine;
import by.tsuprikova.smvservice.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.ResponseRepository;

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
import org.springframework.web.bind.MethodArgumentNotValidException;


import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
    private NaturalPersonRequestRepository requestRepository;

    @MockBean
    private ResponseRepository responseRepository;

    private NaturalPersonRequest naturalPersonRequest;
    private ResponseWithFine responseWithFine;


    @BeforeEach
    void init() {

        naturalPersonRequest = new NaturalPersonRequest();
        String sts = "59 ут 123456";
        naturalPersonRequest.setSts(sts);
        naturalPersonRequest.setId(UUID.randomUUID());
        responseWithFine = new ResponseWithFine();
        BigDecimal amountOfAccrual = new BigDecimal(28);
        BigDecimal amountOfPaid = new BigDecimal(28);
        int numberOfResolution = 321521;
        String articleOfKoap = "21.3";
        responseWithFine.setSts(sts);
        responseWithFine.setAmountOfAccrual(amountOfAccrual);
        responseWithFine.setArticleOfKoap(articleOfKoap);
        responseWithFine.setAmountOfPaid(amountOfPaid);
        responseWithFine.setNumberOfResolution(numberOfResolution);


    }


    @Test
    void SaveValidNaturalPersonRequestTest() throws Exception {

        Mockito.doReturn(naturalPersonRequest).when(requestRepository).save(any(NaturalPersonRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value(naturalPersonRequest.getSts())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());


    }


    @Test
    void SaveInValidNaturalPersonRequestTest() throws Exception {

        NaturalPersonRequest invalidRequest = new NaturalPersonRequest();
        invalidRequest.setSts("");

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(invalidRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value("поле стс не может быть пустое")).
                andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));

    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {

        Mockito.when(responseRepository.findBySts(any(String.class))).thenReturn(responseWithFine);

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

        Mockito.when(responseRepository.findBySts(any(String.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

    }


    @Test
    void deleteResponseWithFineByValidId() throws Exception {

        int kol = 1;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/smv/natural_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.OK.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }

    @Test
    void deleteResponseWithFineByInValidId() throws Exception {

        int kol = 0;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/smv/natural_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }


}
