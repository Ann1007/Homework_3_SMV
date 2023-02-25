package by.tsuprikova.smvservice.integration;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.model.ResponseWithFine;
import by.tsuprikova.smvservice.repositories.LegalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.LegalPersonResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import java.util.Date;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ForLegalPersonIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LegalPersonResponseRepository responseRepository;

    @Autowired
    private LegalPersonRequestRepository requestRepository;

    private LegalPersonRequest legalPersonRequest;


    @BeforeEach
    void init() {

        legalPersonRequest = new LegalPersonRequest();
        Long inn = 4567832109L;
        legalPersonRequest.setInn(inn);

    }


    @Test
    void SaveValidLegalPersonRequestTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/legal_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(legalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.inn").value(legalPersonRequest.getInn())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }


    @Test
    void saveInvalidLegalPersonRequestTest() throws Exception {

        LegalPersonRequest invalidRequest = new LegalPersonRequest();
        invalidRequest.setInn(544L);

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/legal_person/save_request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(invalidRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.inn").value("поле ИНН должно состоять минимум из 10 цифр")).
                andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));

    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {
        requestRepository.save(legalPersonRequest);
        Thread.sleep(1000);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/smv/legal_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(legalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();

        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        LegalPersonResponse resultResponseWithFine = objectMapper.readValue(resultContext, LegalPersonResponse.class);

        Assertions.assertNotNull(resultResponseWithFine);
        Assertions.assertEquals(4567832109L, resultResponseWithFine.getInn());
        Assertions.assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfAccrual());
        Assertions.assertEquals(123, resultResponseWithFine.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(44), resultResponseWithFine.getAmountOfPaid());
        Assertions.assertEquals("21.1", resultResponseWithFine.getArticleOfKoap());

    }


    @Test
    void getResponseWithFineByStIsNull() throws Exception {

        LegalPersonRequest wrongRequest = new LegalPersonRequest();
        wrongRequest.setInn(5445676876L);

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/legal_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(wrongRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

    }


    @Test
    void deleteResponseWithFineByValidId() throws Exception {

        requestRepository.save(legalPersonRequest);
        Thread.sleep(1000);

        ResponseWithFine response1 = responseRepository.findByINN(legalPersonRequest.getInn());
        UUID id = response1.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/smv/legal_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

    }


    @Test
    void deleteResponseWithFineByInValidId() throws Exception {

        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/smv/legal_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));


    }
}