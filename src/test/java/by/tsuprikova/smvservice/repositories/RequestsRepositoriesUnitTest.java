package by.tsuprikova.smvservice.repositories;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.LegalPersonRequest;
import by.tsuprikova.smvservice.model.NaturalPersonRequest;
import by.tsuprikova.smvservice.repositories.impl.LegalPersonRequestRepositoryImpl;
import by.tsuprikova.smvservice.repositories.impl.NaturalPersonRequestRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@ActiveProfiles("test")
public class RequestsRepositoriesUnitTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    private LegalPersonRequestRepository legalRequestRepository;
    private NaturalPersonRequestRepository naturalRequestRepository;

    private LegalPersonRequest legalPersonRequest;
    private NaturalPersonRequest naturalPersonRequest;


    @BeforeEach
    void init() {
        legalRequestRepository = new LegalPersonRequestRepositoryImpl(jdbcTemplate);
        naturalRequestRepository = new NaturalPersonRequestRepositoryImpl(jdbcTemplate);

        legalPersonRequest = new LegalPersonRequest();
        legalPersonRequest.setInn(1234567890L);

        naturalPersonRequest = new NaturalPersonRequest();
        String sts = "59 ут 123456";
        naturalPersonRequest.setSts(sts);

    }


    @Test
    void saveNaturalPersonRequestTest() throws SmvServerException {
        NaturalPersonRequest insertRequest = naturalRequestRepository.save(naturalPersonRequest);
        assertNotNull(insertRequest);
        assertThat(insertRequest.getId()).isNotNull();
    }


    @Test
    void getNaturalPersonRequestByValidIdTest() throws SmvServerException {
        NaturalPersonRequest insertRequest = naturalRequestRepository.save(naturalPersonRequest);
        NaturalPersonRequest resultRequest = naturalRequestRepository.getById(insertRequest.getId());
        assertNotNull(insertRequest);
        assertEquals("59 ут 123456", resultRequest.getSts());

    }


    @Test
    void getNaturalPersonRequestByInvalidIdTest() {
        UUID id = UUID.randomUUID();
        assertThrows(SmvServerException.class, () -> naturalRequestRepository.getById(id));

    }


    @Test
    void deleteNaturalPersonRequestByValidIdTest() throws SmvServerException {
        NaturalPersonRequest insertRequest = naturalRequestRepository.save(naturalPersonRequest);
        int result = naturalRequestRepository.delete(insertRequest.getId());
        assertEquals(1, result);

    }


    @Test
    void deleteNaturalPersonRequestByInvalidIdTest() throws SmvServerException {

        int result = naturalRequestRepository.delete(UUID.randomUUID());
        assertEquals(0, result);

    }


    @Test
    void saveLegalPersonRequestTest() throws SmvServerException {
        LegalPersonRequest insertRequest = legalRequestRepository.save(legalPersonRequest);
        assertNotNull(insertRequest);
        assertThat(insertRequest.getId()).isNotNull();
    }


    @Test
    void getLegalPersonRequestByValidIdTest() throws SmvServerException {
        LegalPersonRequest insertRequest = legalRequestRepository.save(legalPersonRequest);
        LegalPersonRequest resultRequest = legalRequestRepository.getById(insertRequest.getId());
        assertNotNull(insertRequest);
        assertEquals(1234567890L, resultRequest.getInn());

    }


    @Test
    void getLegalPersonRequestByInvalidIdTest() {
        UUID id = UUID.randomUUID();
        assertThrows(SmvServerException.class, () -> legalRequestRepository.getById(id));

    }


    @Test
    void deleteLegalPersonRequestByValidIdTest() throws SmvServerException {
        LegalPersonRequest insertRequest = legalRequestRepository.save(legalPersonRequest);
        int result = legalRequestRepository.delete(insertRequest.getId());
        assertEquals(1, result);

    }


    @Test
    void deleteLegalPersonRequestByInvalidIdTest() throws SmvServerException {

        int result = legalRequestRepository.delete(UUID.randomUUID());
        assertEquals(0, result);

    }


}
