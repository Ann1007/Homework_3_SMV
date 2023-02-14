package by.tsuprikova.SMVService.repositories.impl;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.ResponseWithFine;
import by.tsuprikova.SMVService.repositories.ResponseRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ResponseRepositoryImpl implements ResponseRepository {

    private final String SELECT_RESPONSE_BY_STS =
            "SELECT id, amount_of_accrual, amount_of_paid, number_of_resolution, sts, date_of_resolution, article_of_koap FROM response_with_fine WHERE sts=?";

    private final String INSERT_RESPONSE = "INSERT INTO response_with_fine " +
            "(id,amount_of_accrual, amount_of_paid, article_of_koap, date_of_resolution, number_of_resolution, sts) VALUES (?,?,?,?,?,?,?)";

    private final String DELETE_RESPONSE_BY_ID = "DELETE FROM response_with_fine WHERE id=?";


    private final JdbcTemplate jdbcTemplate;

    @Override
    public ResponseWithFine findBySts(String sts) throws SmvServerException {

        ResponseWithFine response = null;
        try {
            response = jdbcTemplate.queryForObject(SELECT_RESPONSE_BY_STS, new Object[]{sts},
                    (rs, numRow) ->
                            new ResponseWithFine(
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
            throw new SmvServerException(e.getMessage());
        }
        return response;
    }

    @Override
    public void save(ResponseWithFine response) throws SmvServerException {
        try {
            response.setId(UUID.randomUUID());
            jdbcTemplate.update(INSERT_RESPONSE, response.getId(), response.getAmountOfAccrual(), response.getAmountOfPaid(),
                    response.getArticleOfKoap(), response.getDateOfResolution(), response.getNumberOfResolution(), response.getSts());

        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
    }

    @Override
    public void deleteById(UUID id) throws SmvServerException {
        try {
            jdbcTemplate.update(DELETE_RESPONSE_BY_ID, id);
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
    }


}
