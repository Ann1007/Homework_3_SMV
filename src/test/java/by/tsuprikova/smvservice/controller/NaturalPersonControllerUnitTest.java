package by.tsuprikova.smvservice.controller;


import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.repositories.NaturalPersonRequestRepository;
import by.tsuprikova.smvservice.repositories.NaturalPersonResponseRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Cleanup;
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

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class NaturalPersonControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private NaturalPersonRequestRepository requestRepository;

    @MockBean
    private NaturalPersonResponseRepository responseRepository;

    private NaturalPersonRequest naturalPersonRequest;
    private NaturalPersonResponse response;


    @BeforeEach
    void init() {

        naturalPersonRequest = new NaturalPersonRequest();
        String sts = "59 ???? 123456";
        naturalPersonRequest.setSts(sts);
        naturalPersonRequest.setId(UUID.randomUUID());

        response = new NaturalPersonResponse();
        BigDecimal amountOfAccrual = new BigDecimal(28);
        BigDecimal amountOfPaid = new BigDecimal(28);
        int numberOfResolution = 321521;
        String articleOfKoap = "21.3";
        response.setSts(sts);
        response.setAmountOfAccrual(amountOfAccrual);
        response.setArticleOfKoap(articleOfKoap);
        response.setAmountOfPaid(amountOfPaid);
        response.setNumberOfResolution(numberOfResolution);

    }

    @Test
    void checkBadNaturalPersonRequestTest() throws Exception {

        NaturalPersonRequest invalidRequest = new NaturalPersonRequest();
        invalidRequest.setSts("");

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(invalidRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value("the sts field cannot be empty"));

        JAXBContext context = JAXBContext.newInstance(NaturalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(invalidRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/request").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        accept(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.BAD_REQUEST.value())).
                andExpect(MockMvcResultMatchers.xpath("//sts/text()").string("the sts field cannot be empty"));

    }


    @Test
    void saveValidJsonRequestTest() throws Exception {

        Mockito.doReturn(naturalPersonRequest).when(requestRepository).save(any(NaturalPersonRequest.class));
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/request").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.ACCEPTED.value())).
                andExpect(MockMvcResultMatchers.jsonPath("$.sts").value(naturalPersonRequest.getSts())).
                andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());

    }

    @Test
    void saveValidXmlRequestTest() throws Exception {

        Mockito.doReturn(naturalPersonRequest).when(requestRepository).save(any(NaturalPersonRequest.class));
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

        Mockito.when(responseRepository.findBySts(any(String.class))).thenReturn(response);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value())).
                andReturn();


        String resultContext = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        NaturalPersonResponse resultResponseWithFine = objectMapper.readValue(resultContext, NaturalPersonResponse.class);

        Assertions.assertNotNull(resultResponseWithFine);
        Assertions.assertEquals("59 ???? 123456", resultResponseWithFine.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfAccrual());
        Assertions.assertEquals(321521, resultResponseWithFine.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponseWithFine.getArticleOfKoap());

    }


    @Test
    void getNotNullXmlResponseByValidXmlRequestTest() throws Exception {

        Mockito.when(responseRepository.findBySts(any(String.class))).thenReturn(response);

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
        NaturalPersonResponse resultResponseWithFine = (NaturalPersonResponse) unmarshaller.unmarshal(stringReader);

        Assertions.assertNotNull(resultResponseWithFine);
        Assertions.assertEquals("59 ???? 123456", resultResponseWithFine.getSts());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfAccrual());
        Assertions.assertEquals(321521, resultResponseWithFine.getNumberOfResolution());
        Assertions.assertEquals(new BigDecimal(28), resultResponseWithFine.getAmountOfPaid());
        Assertions.assertEquals("21.3", resultResponseWithFine.getArticleOfKoap());

    }


    @Test
    void getNullResponseTest() throws Exception {

        Mockito.when(responseRepository.findBySts(any(String.class))).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_JSON).
                        content(objectMapper.writeValueAsString(naturalPersonRequest))).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

        JAXBContext context = JAXBContext.newInstance(NaturalPersonRequest.class);
        Marshaller mar = context.createMarshaller();
        mar.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        @Cleanup StringWriter sw = new StringWriter();
        mar.marshal(naturalPersonRequest, sw);
        String xmlRequest = sw.toString();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/smv/natural_person/response").
                        contentType(MediaType.APPLICATION_XML_VALUE).
                        content(xmlRequest)).
                andExpect(MockMvcResultMatchers.status().is(HttpStatus.NOT_FOUND.value()));

    }


    @Test
    void deleteResponseByValidId() throws Exception {

        int kol = 1;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/api/v1/smv/natural_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.OK.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }


    @Test
    void deleteResponseByInvalidId() throws Exception {

        int kol = 0;
        UUID id = UUID.randomUUID();
        Mockito.when(responseRepository.deleteById(any(UUID.class))).thenReturn(kol);

        mockMvc.perform(delete("/api/v1/smv/natural_person/response/{id}", id)).
                andExpect(status().is(HttpStatus.METHOD_NOT_ALLOWED.value()));

        Mockito.verify(responseRepository, Mockito.times(1)).deleteById(id);

    }


}
