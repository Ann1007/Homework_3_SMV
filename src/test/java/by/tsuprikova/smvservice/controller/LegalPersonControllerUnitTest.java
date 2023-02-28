package by.tsuprikova.smvservice.controller;


import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.repositories.LegalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.LegalPersonResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LegalPersonControllerUnitTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LegalPersonRequestRepository requestRepository;

    @MockBean
    private LegalPersonResponseRepository responseRepository;

    private LegalPersonRequest request;
    private LegalPersonResponse response;

   @BeforeEach
    void init() {

        Long inn = 1234567890L;
        request = new LegalPersonRequest();
        request.setId(UUID.randomUUID());
        request.setInn(inn);

        response = new LegalPersonResponse();

        BigDecimal amountOfAccrual = new BigDecimal(44);
        BigDecimal amountOfPaid = new BigDecimal(44);
        int numberOfResolution = 1212;
        String articleOfKoap = "32.1";
        response.setInn(inn);
        response.setAmountOfAccrual(amountOfAccrual);
        response.setArticleOfKoap(articleOfKoap);
        response.setAmountOfPaid(amountOfPaid);
        response.setId(UUID.randomUUID());
        response.setNumberOfResolution(numberOfResolution);
    }


    @Test
    void saveValidLegalPersonRequest() throws Exception {

        Mockito.doReturn(request).when(requestRepository).save(any(LegalPersonRequest.class));

        mockMvc.perform(post("/api/v1/smv/legal_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.ACCEPTED.value())).
                andExpect(jsonPath("$.id").exists()).
                andExpect(jsonPath("$.inn").value(request.getInn()));
    }


    @Test
    void SaveInValidNaturalPersonRequestTest() throws Exception {

        LegalPersonRequest invalidRequest = new LegalPersonRequest();
        invalidRequest.setInn(544L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/legal_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(invalidRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.inn").value("the inn field must consist of at least 10 digits")).
                andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));

    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {

        Mockito.when(responseRepository.findByINN(any(Long.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(post("/api/v1/smv/legal_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.OK.value())).
                andReturn();

        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        LegalPersonResponse resultResponseWithFine = objectMapper.readValue(resultContext, LegalPersonResponse.class);
        assertNotNull(resultResponseWithFine);
        assertEquals(1234567890L, resultResponseWithFine.getInn());
        assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfAccrual());
        assertEquals(1212, resultResponseWithFine.getNumberOfResolution());
        assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfPaid());
        assertEquals("32.1", resultResponseWithFine.getArticleOfKoap());
    }


    @Test
    void getResponseWithFineByStIsNull() throws Exception {

        Mockito.when(responseRepository.findByINN(any(Long.class))).thenReturn(null);

        mockMvc.perform(post("/api/v1/smv/legal_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(request))).
                andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }


    @Test
    void deleteResponseWithFineByValidId() throws Exception {

        int kol = 1;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/api/v1/smv/legal_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.OK.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }

    @Test
    void deleteResponseWithFineByInValidId() throws Exception {

        int kol = 0;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/api/v1/smv/legal_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }

}
