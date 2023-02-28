package by.tsuprikova.smvservice.repositories.impl;

import by.tsuprikova.smvservice.exceptions.SmvServiceException;
import by.tsuprikova.smvservice.model.InfoOfFineNaturalPerson;
import by.tsuprikova.smvservice.repositories.NaturalPersonInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NaturalPersonInfoRepositoryImpl implements NaturalPersonInfoRepository {

    private final String SELECT_INFO_OF_FINE_BY_STS =
            "SELECT id, amount_of_accrual, amount_of_paid, number_of_resolution, sts, date_of_resolution, article_of_koap " +
                    "FROM natural_person_fine_info WHERE sts=?";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public InfoOfFineNaturalPerson findBySts(String sts) throws SmvServiceException {
        InfoOfFineNaturalPerson info = null;
        try {

            info = jdbcTemplate.queryForObject(SELECT_INFO_OF_FINE_BY_STS, new Object[]{sts},
                    (rs, numRow) ->
                            new InfoOfFineNaturalPerson(
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
        return info;
    }
}
