package org.tmc.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.tmc.data.mappers.CuisineMapper;
import org.tmc.models.Cuisine;

import java.util.List;

@Repository
public class CuisineJdbcTemplateRepository implements CuisineRepository {

    private final JdbcTemplate jdbcTemplate;

    public CuisineJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Cuisine> findAll() {
        final String sql = "select cuisine_id, name "
                + "from cuisine;";

        return jdbcTemplate.query(sql, new CuisineMapper());
    }

}