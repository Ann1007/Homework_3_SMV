package by.tsuprikova.smvservice.integration;


import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.model.Response;
import by.tsuprikova.smvservice.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.NaturalPersonResponseRepository;
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
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
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
    void saveValidJsonNaturalPersonRequestTest() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value(naturalPersonRequest.getSts())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }


    @Test
    void saveValidXmlNaturalPersonRequestTest() throws Exception {

        JAXBContext context = JAXBContext.newInstance(NaturalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(naturalPersonRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/request").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.xpath("//sts/text()").string(naturalPersonRequest.getSts())).
                andExpect(MockMvcResultMatchers.xpath("//id/text()").exists());
    }


    @Test
    void getNotNullJsonResponseByValidJsonRequestTest() throws Exception {

        requestRepository.save(naturalPersonRequest);

        Thread.sleep(1000);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();

        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        NaturalPersonResponse resultResponse = objectMapper.readValue(resultContext, NaturalPersonResponse.class);

        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals("98 ут 253901", resultResponse.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponse.getAmountOfAccrual());
        Assertions.assertEquals(323121, resultResponse.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponse.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponse.getArticleOfKoap());

    }


    @Test
    void getNotNullXmlResponseByValidXmlRequestTest() throws Exception {

        requestRepository.save(naturalPersonRequest);
        Thread.sleep(1000);

        JAXBContext context = JAXBContext.newInstance(NaturalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(naturalPersonRequest, sw);
        String xmlRequest = sw.toString();

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();

        String resultContext = result.getResponse().getContentAsString();
        JAXBContext jaxbContext = JAXBContext.newInstance(NaturalPersonResponse.class);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        @Cleanup StringReader stringReader = new StringReader(resultContext);
        NaturalPersonResponse resultResponse = (NaturalPersonResponse) unmarshaller.unmarshal(stringReader);

        Assertions.assertNotNull(resultResponse);
        Assertions.assertEquals("98 ут 253901", resultResponse.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponse.getAmountOfAccrual());
        Assertions.assertEquals(323121, resultResponse.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponse.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponse.getArticleOfKoap());

    }


    @Test
    void getNullResponseTest() throws Exception {

        NaturalPersonRequest wrongRequest = new NaturalPersonRequest();
        wrongRequest.setSts("33 ув 435654");

        Response response = responseRepository.findBySts(wrongRequest.getSts());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(wrongRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

        JAXBContext context = JAXBContext.newInstance(NaturalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(wrongRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

        Assertions.assertNull(response);

    }


    @Test
    void deleteResponseByValidId() throws Exception {

        requestRepository.save(naturalPersonRequest);
        Thread.sleep(1000);
        Response response = responseRepository.findBySts(naturalPersonRequest.getSts());
        UUID id = response.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/smv/natural_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }


    @Test
    void deleteResponseInValidId() throws Exception {

        UUID id = UUID.randomUUID();
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/smv/natural_person/response/{id}", id)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));

    }


}
