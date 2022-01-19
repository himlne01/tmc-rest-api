package org.tmc.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.tmc.models.Cuisine;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CuisineMapper implements RowMapper<Cuisine> {

    @Override
    public Cuisine mapRow(ResultSet resultSet, int i) throws SQLException {
        Cuisine cuisine = new Cuisine();
        cuisine.setCuisineId(resultSet.getInt("cuisine_id"));
        cuisine.setCuisineName(resultSet.getString("name"));
        return cuisine;
    }
}
