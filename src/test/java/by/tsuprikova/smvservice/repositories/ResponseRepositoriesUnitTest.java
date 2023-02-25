package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.repositories.impl.LegalPersonResponseRepositoryImpl;
import by.tsuprikova.smvservice.repositories.impl.NaturalPersonResponseRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@ActiveProfiles("test")
public class ResponseRepositoriesUnitTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LegalPersonResponseRepository legalResponseRepository;
    private NaturalPersonResponseRepository naturalResponseRepository;
    private NaturalPersonResponse naturalPersonResponse;
    private LegalPersonResponse legalPersonResponse;

    @BeforeEach
    void init() {
        legalResponseRepository = new LegalPersonResponseRepositoryImpl(jdbcTemplate);
        naturalResponseRepository = new NaturalPersonResponseRepositoryImpl(jdbcTemplate);

        String sts = "59 ут 123456";
        Long inn = 1234567890L;
        BigDecimal amountOfAccrual = new BigDecimal(28);
        BigDecimal amountOfPaid = new BigDecimal(28);
        int numberOfResolution = 321521;
        String articleOfKoap = "21.3";
        Date date = new Date();

        naturalPersonResponse = new NaturalPersonResponse();
        naturalPersonResponse.setSts(sts);
        naturalPersonResponse.setAmountOfAccrual(amountOfAccrual);
        naturalPersonResponse.setArticleOfKoap(articleOfKoap);
        naturalPersonResponse.setAmountOfPaid(amountOfPaid);
        naturalPersonResponse.setNumberOfResolution(numberOfResolution);
        naturalPersonResponse.setDateOfResolution(date);

        legalPersonResponse = new LegalPersonResponse();
        legalPersonResponse.setInn(inn);
        legalPersonResponse.setAmountOfAccrual(amountOfAccrual);
        legalPersonResponse.setArticleOfKoap(articleOfKoap);
        legalPersonResponse.setAmountOfPaid(amountOfPaid);
        legalPersonResponse.setNumberOfResolution(numberOfResolution);
        legalPersonResponse.setDateOfResolution(date);


    }


    @Test
    void saveNaturalPersonResponseTest() throws SmvServerException {
        naturalResponseRepository.save(naturalPersonResponse);
        NaturalPersonResponse createdResponse = naturalResponseRepository.findBySts(naturalPersonResponse.getSts());
        assertThat(createdResponse.getId()).isNotNull();
        assertThat(createdResponse).isNotNull();

    }


    @Test
    void getNaturalPersonResponseByValidStsTest() throws SmvServerException {
        naturalResponseRepository.save(naturalPersonResponse);
        NaturalPersonResponse response = naturalResponseRepository.findBySts(naturalPersonResponse.getSts());
        assertThat(response).isNotNull();
        assertEquals("59 ут 123456", response.getSts());

    }


    @Test
    void getNaturalPersonResponseByInvalidStsTest() throws SmvServerException {
        String invalidSts = "34 00 67786";
        NaturalPersonResponse response = naturalResponseRepository.findBySts(invalidSts);
        assertThat(response).isNull();

    }


    @Test
    void deleteNaturalPersonResponseByValidId() throws SmvServerException {
        naturalResponseRepository.save(naturalPersonResponse);
        NaturalPersonResponse response = naturalResponseRepository.findBySts(naturalPersonResponse.getSts());
        int result = naturalResponseRepository.deleteById(response.getId());
        assertEquals(1, result);
    }


    @Test
    void deleteNaturalPersonResponseByInvalidId() throws SmvServerException {

        int result = naturalResponseRepository.deleteById(UUID.randomUUID());
        assertEquals(0, result);
    }


    @Test
    void saveLegalPersonResponseTest() throws SmvServerException {
        legalResponseRepository.save(legalPersonResponse);
        LegalPersonResponse createdResponse = legalResponseRepository.findByINN(legalPersonResponse.getInn());
        assertThat(createdResponse.getId()).isNotNull();
        assertThat(createdResponse).isNotNull();
    }


    @Test
    void getLegalPersonResponseByValidStsTest() throws SmvServerException {
        legalResponseRepository.save(legalPersonResponse);
        LegalPersonResponse response = legalResponseRepository.findByINN(legalPersonResponse.getInn());
        assertThat(response).isNotNull();
        assertEquals(1234567890L, response.getInn());

    }


    @Test
    void getLegalPersonResponseByInvalidStsTest() throws SmvServerException {
        Long invalidInn = 12222L;
        LegalPersonResponse response = legalResponseRepository.findByINN(invalidInn);
        assertThat(response).isNull();

    }


    @Test
    void deleteLegalPersonResponseByValidId() throws SmvServerException {
        legalResponseRepository.save(legalPersonResponse);
        LegalPersonResponse response = legalResponseRepository.findByINN(legalPersonResponse.getInn());
        int result = legalResponseRepository.deleteById(response.getId());
        assertEquals(1, result);
    }


    @Test
    void deleteLegalPersonResponseByInvalidId() throws SmvServerException {

        int result = legalResponseRepository.deleteById(UUID.randomUUID());
        assertEquals(0, result);
    }


}
