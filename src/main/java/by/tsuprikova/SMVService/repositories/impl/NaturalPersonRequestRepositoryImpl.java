package by.tsuprikova.SMVService.repositories.impl;

import by.tsuprikova.SMVService.exceptions.SmvServerException;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.repositories.NaturalPersonRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class NaturalPersonRequestRepositoryImpl implements NaturalPersonRequestRepository {

    private final String INSERT_REQUEST = "INSERT INTO natural_person_request (id,sts) VALUES (?,?)";
    private final String SELECT_FIRST_ID = "SELECT id FROM natural_person_request LIMIT 1";
    private final String SELECT_REQUEST_BY_ID = "SELECT id,sts FROM natural_person_request WHERE id=?";
    private final String DELETE_REQUEST_BY_ID = "DELETE FROM natural_person_request WHERE id=?";

    private final JdbcTemplate jdbcTemplate;


    @Override
    public UUID findFirstId() throws SmvServerException {
        UUID id = null;
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
    public NaturalPersonRequest save(NaturalPersonRequest naturalPersonRequest) throws SmvServerException {
        NaturalPersonRequest insertRequest;
        try {
            UUID id = UUID.randomUUID();
            naturalPersonRequest.setId(id);
            jdbcTemplate.update(INSERT_REQUEST, naturalPersonRequest.getId(), naturalPersonRequest.getSts());
            insertRequest = getById(id);

        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }

        return insertRequest;

    }


    @Override
    public NaturalPersonRequest getById(UUID id) throws SmvServerException {
        NaturalPersonRequest request;
        try {
            request = jdbcTemplate.queryForObject(SELECT_REQUEST_BY_ID, new Object[]{id},
                    (rs, rowNum) ->
                            new NaturalPersonRequest(
                                    rs.getObject("id", java.util.UUID.class),
                                    rs.getString("sts")
                            )
            );
        } catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }
        return request;
    }


    @Override
    public void delete(UUID id) throws SmvServerException {
        try {
            jdbcTemplate.update(DELETE_REQUEST_BY_ID, id);
        }catch (DataAccessException e) {
            throw new SmvServerException(e.getMessage());
        }


    }
}
