package by.tsuprikova.smvservice.integration;


import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.model.ResponseWithFine;
import by.tsuprikova.smvservice.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.NaturalPersonResponseRepository;
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
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ForNaturalPersonIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NaturalPersonResponseRepository responseRepository;

    @Autowired
    private NaturalPersonRequestRepository requestRepository;

    private NaturalPersonRequest naturalPersonRequest;

    @BeforeEach
    void init() {

        naturalPersonRequest = new NaturalPersonRequest();
        String sts = "98 ут 253901";
        naturalPersonRequest.setSts(sts);

    }


    @Test
    void SaveValidNaturalPersonRequestTest() throws Exception {

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
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value("the sts field cannot be empty")).
                andExpect(mvcResult -> mvcResult.getResolvedException().getClass().equals(MethodArgumentNotValidException.class));

    }


    @Test
    void getResponseWithFineByStsNotNull() throws Exception {

        requestRepository.save(naturalPersonRequest);

        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();


        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        NaturalPersonResponse resultResponseWithFine = objectMapper.readValue(resultContext, NaturalPersonResponse.class);

        Assertions.assertNotNull(resultResponseWithFine);
        Assertions.assertEquals("98 ут 253901", resultResponseWithFine.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfAccrual());
        Assertions.assertEquals(323121, resultResponseWithFine.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponseWithFine.getArticleOfKoap());


    }


    @Test
    void getResponseWithFineByStIsNull() throws Exception {

        NaturalPersonRequest wrongRequest = new NaturalPersonRequest();
        wrongRequest.setSts("33 ув 435654");

        ResponseWithFine response = responseRepository.findBySts(wrongRequest.getSts());

        mockMvc.perform(MockMvcRequestBuilders.post("/smv/natural_person/get_response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(wrongRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

        Assertions.assertNull(response);

    }


    @Test
    void deleteResponseWithFineByValidId() throws Exception {

        requestRepository.save(naturalPersonRequest);
        Thread.sleep(1000);
        ResponseWithFine response = responseRepository.findBySts(naturalPersonRequest.getSts());
        UUID id = response.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/smv/natural_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

    }


    @Test
    void deleteResponseWithFineByInValidId() throws Exception {

        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/smv/natural_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));

    }


}
