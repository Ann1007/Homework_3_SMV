package by.tsuprikova.smvservice.repositories.impl;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.NaturalPersonResponse;
import by.tsuprikova.smvservice.repositories.NaturalPersonResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NaturalPersonResponseRepositoryImpl implements NaturalPersonResponseRepository {

    private final String SELECT_RESPONSE_BY_STS =
            "SELECT id, amount_of_accrual, amount_of_paid, number_of_resolution, sts, date_of_resolution, article_of_koap FROM natural_person_response WHERE sts=?";

    private final String INSERT_RESPONSE = "INSERT INTO natural_person_response " +
            "(id,amount_of_accrual, amount_of_paid, article_of_koap, date_of_resolution, number_of_resolution, sts) VALUES (?,?,?,?,?,?,?)";

    private final String DELETE_RESPONSE_BY_ID = "DELETE FROM natural_person_response WHERE id=?";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public NaturalPersonResponse findBySts(String sts) throws SmvServiceException {

        NaturalPersonResponse response = null;
        try {
            response = jdbcTemplate.queryForObject(SELECT_RESPONSE_BY_STS, new Object[]{sts},
                    (rs, numRow) ->
                            new NaturalPersonResponse(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getBigDecimal("amount_of_accrual"),
                                    rs.getBigDecimal("amount_of_paid"),
                                    rs.getInt("number_of_resolution"),
                                    rs.getString("sts"),
                                    rs.getDate("date_of_resolution"),
                                    rs.getString("article_of_koap")
                            )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new SmvServiceException(e.getMessage());
        }
        return response;
    }

    @Override
    public void save(NaturalPersonResponse response) throws SmvServiceException {
        try {
            response.setId(UUID.randomUUID());
            jdbcTemplate.update(INSERT_RESPONSE, response.getId(), response.getAmountOfAccrual(), response.getAmountOfPaid(),
                    response.getArticleOfKoap(), response.getDateOfResolution(), response.getNumberOfResolution(), response.getSts());

        } catch (DataAccessException e) {
            throw new SmvServiceException(e.getMessage());
        }
    }

    @Override
    public int deleteById(UUID id) throws SmvServiceException {

        int kol = 0;
        try {
            kol = jdbcTemplate.update(DELETE_RESPONSE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new SmvServiceException(e.getMessage());
        }
        return kol;
    }


}
