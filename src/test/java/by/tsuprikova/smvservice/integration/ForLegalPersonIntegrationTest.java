package by.tsuprikova.smvservice.integration;

import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.model.Response;
import by.tsuprikova.smvservice.repositories.LegalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.LegalPersonResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
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

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void saveValidJsonLegalPersonRequestTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/legal_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(legalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.inn").value(legalPersonRequest.getInn())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    void saveValidXmlRequestTest() throws Exception {
        JAXBContext context = JAXBContext.newInstance(LegalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(legalPersonRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/legal_person/request").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.xpath("//inn/text()").string(legalPersonRequest.getInn().toString())).
                andExpect(MockMvcResultMatchers.xpath("//id/text()").exists());

    }




    @Test
    void getNotNullJsonResponseByValidJsonRequestTest() throws Exception {
        requestRepository.save(legalPersonRequest);
        Thread.sleep(1000);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/legal_person/response").
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
    void getNotNullXmlResponseByValidXmlRequestTest() throws Exception {

        requestRepository.save(legalPersonRequest);
        Thread.sleep(1000);

        JAXBContext context = JAXBContext.newInstance(LegalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(legalPersonRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(post("/api/v1/smv/legal_person/response").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(status().is(HttpStatus.OK.value())).
                andExpect(MockMvcResultMatchers.xpath("//inn/text()").string("4567832109")).
                andExpect(MockMvcResultMatchers.xpath("//amountOfAccrual/text()").string("44")).
                andExpect(MockMvcResultMatchers.xpath("//amountOfPaid/text()").string("44")).
                andExpect(MockMvcResultMatchers.xpath("//numberOfResolution/text()").string("123")).
                andExpect(MockMvcResultMatchers.xpath("//articleOfKoap/text()").string("21.1"));


    }


    @Test
    void getNullResponseTest() throws Exception {

        LegalPersonRequest wrongRequest = new LegalPersonRequest();
        wrongRequest.setInn(5445676876L);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/legal_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(wrongRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

        JAXBContext context = JAXBContext.newInstance(LegalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(wrongRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(post("/api/v1/smv/legal_person/response").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(status().is(HttpStatus.NOT_FOUND.value()));

    }


    @Test
    void deleteResponseByValidId() throws Exception {

        requestRepository.save(legalPersonRequest);
        Thread.sleep(1000);

        Response response1 = responseRepository.findByINN(legalPersonRequest.getInn());
        UUID id = response1.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/smv/legal_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));

    }


    @Test
    void deleteResponseByInValidId() throws Exception {

        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/smv/legal_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));


    }
}