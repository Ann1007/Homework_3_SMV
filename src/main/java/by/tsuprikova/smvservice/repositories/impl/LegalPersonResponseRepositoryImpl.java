package by.tsuprikova.smvservice.repositories.impl;

import by.tsuprikova.smvservice.exceptions.SmvServerException;
import by.tsuprikova.smvservice.model.LegalPersonResponse;
import by.tsuprikova.smvservice.repositories.LegalPersonResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LegalPersonResponseRepositoryImpl implements LegalPersonResponseRepository {

    private final String SELECT_RESPONSE_BY_INN =
            "SELECT id, amount_of_accrual, amount_of_paid, number_of_resolution, inn, date_of_resolution, article_of_koap FROM legal_person_response WHERE inn=?";

    private final String INSERT_RESPONSE = "INSERT INTO legal_person_response " +
            "(id,amount_of_accrual, amount_of_paid, article_of_koap, date_of_resolution, number_of_resolution, inn) VALUES (?,?,?,?,?,?,?)";

    private final String DELETE_RESPONSE_BY_ID = "DELETE FROM legal_person_response WHERE id=?";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public LegalPersonResponse findByINN(Long INN) throws SmvServerException {
        LegalPersonResponse response = null;
        try {
            response = jdbcTemplate.queryForObject(SELECT_RESPONSE_BY_INN, new Object[]{INN},
                    (rs, numRow) ->
                            new LegalPersonResponse(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getBigDecimal("amount_of_accrual"),
                                    rs.getBigDecimal("amount_of_paid"),
                                    rs.getInt("number_of_resolution"),
                                    rs.getLong("inn"),
                                    rs.getDate("date_of_resolution"),
                                    rs.getString("article_of_koap")
                            )
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return response;
    }


    @Override
    public void save(LegalPersonResponse response) throws SmvServerException {
        try {
            response.setId(UUID.randomUUID());
            jdbcTemplate.update(INSERT_RESPONSE, response.getId(), response.getAmountOfAccrual(), response.getAmountOfPaid(),
                    response.getArticleOfKoap(), response.getDateOfResolution(), response.getNumberOfResolution(), response.getInn());

        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
    }


    @Override
    public int deleteById(UUID id) throws SmvServerException {
        int kol = 0;
        try {
            kol = jdbcTemplate.update(DELETE_RESPONSE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return kol;
    }
}
