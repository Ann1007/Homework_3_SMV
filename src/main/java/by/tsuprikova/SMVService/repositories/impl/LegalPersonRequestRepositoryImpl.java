package by.tsuprikova.SMVService.repositories.impl;

import by.tsuprikova.SMVService.model.LegalPersonRequest;
import by.tsuprikova.SMVService.model.NaturalPersonRequest;
import by.tsuprikova.SMVService.repositories.LegalPersonRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class LegalPersonRequestRepositoryImpl implements LegalPersonRequestRepository {

    // <createTable tableName="legal_person_request" remarks="Создание таблицы для юр. лиц запросов">
    //
    //            <column name="id" type="varchar(36)" defaultValueComputed="${type.uuid}" >
    //                <constraints primaryKey="true" nullable="false"/>
    //            </column>
    //            <column name="sts" type="varchar(15)">
    //                <constraints nullable="false"/>
    //            </column>
    //            <column name="inn" type="long">
    //                <constraints nullable="false"/>
    //            </column>
    //        </createTable>

    private final String INSERT_REQUEST = "INSERT INTO legal_person_request (id,sts,inn) VALUES (?,?,?)";
    private final String SELECT_FIRST_ID = "SELECT id FROM legal_person_request LIMIT 1";
    private final String SELECT_REQUEST_BY_ID = "SELECT id,sts,inn FROM legal_person_request WHERE id=?";
    private final String DELETE_REQUEST_BY_ID = "DELETE FROM legal_person_request WHERE id=?";

    private final JdbcTemplate jdbcTemplate;

    @Override
    public UUID findFirstId() {
        UUID id = null;
        try {
            id = jdbcTemplate.queryForObject(SELECT_FIRST_ID, UUID.class);

        } catch (EmptyResultDataAccessException e) {
            //e.printStackTrace();
            return null;
        }
        return id;
    }


    @Override
    public LegalPersonRequest save(LegalPersonRequest request) {
        UUID id = UUID.randomUUID();
        request.setId(id);
        jdbcTemplate.update(INSERT_REQUEST, request.getId(), request.getSts(), request.getINN());
        return getById(id);
    }


    @Override
    public LegalPersonRequest getById(UUID id) {
        return jdbcTemplate.queryForObject(SELECT_REQUEST_BY_ID, new Object[]{id},
                (rs, rowNum) ->
                        new LegalPersonRequest(
                                rs.getObject("id", java.util.UUID.class),
                                rs.getString("sts"),
                                rs.getLong("inn")
                        )
        );
    }


    @Override
    public void delete(UUID id) {
        jdbcTemplate.update(DELETE_REQUEST_BY_ID, id);
    }
}
