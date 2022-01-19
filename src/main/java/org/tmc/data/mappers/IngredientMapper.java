package org.tmc.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.tmc.models.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;

public class IngredientMapper implements RowMapper<Ingredient> {

    @Override
    public Ingredient mapRow(ResultSet resultSet, int i) throws SQLException {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientId(resultSet.getInt("ingredient_id"));
        ingredient.setIngredientName(resultSet.getString("name"));
        ingredient.setQuantity(resultSet.getString("quantity"));
        ingredient.setRecipeId(resultSet.getInt("recipe_id"));
        return ingredient;
    }
}
