package by.tsuprikova.SMVService.repositories.impl;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.repositories.LegalPersonRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LegalPersonRequestRepositoryImpl implements LegalPersonRequestRepository {

    private final String INSERT_REQUEST = "INSERT INTO legal_person_request (id,sts,inn) VALUES (?,?,?)";
    private final String SELECT_FIRST_ID = "SELECT id FROM legal_person_request LIMIT 1";
    private final String SELECT_REQUEST_BY_ID = "SELECT id,sts,inn FROM legal_person_request WHERE id=?";
    private final String DELETE_REQUEST_BY_ID = "DELETE FROM legal_person_request WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UUID findFirstId() throws SmvServerException {
        UUID id;
        try {
            id = jdbcTemplate.queryForObject(SELECT_FIRST_ID, UUID.class);

        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return id;
    }



    @Override
    public LegalPersonRequest save(LegalPersonRequest request) throws SmvServerException {
        LegalPersonRequest insertRequest;
        try {
            UUID id = UUID.randomUUID();
            request.setId(id);
            jdbcTemplate.update(INSERT_REQUEST, request.getId(), request.getSts(), request.getInn());
            insertRequest = getById(id);

        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }

        return insertRequest;
    }


    @Override
    public LegalPersonRequest getById(UUID id) throws SmvServerException {
        LegalPersonRequest request;
        try {
            request = jdbcTemplate.queryForObject(SELECT_REQUEST_BY_ID, new Object[]{id},
                    (rs, rowNum) ->
                            new LegalPersonRequest(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getString("sts"),
                                    rs.getLong("inn")
                            )
            );
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return request;
    }


    @Override
    public int delete(UUID id) throws SmvServerException {
        int kol = 0;
        try {
            kol = jdbcTemplate.update(DELETE_REQUEST_BY_ID, id);
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return kol;

    }
}
