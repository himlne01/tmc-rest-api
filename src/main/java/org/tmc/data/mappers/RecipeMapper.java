package org.tmc.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.tmc.models.Recipe;

import java.sql.ResultSet;
import java.sql.SQLException;

public class RecipeMapper implements RowMapper<Recipe> {

    @Override
    public Recipe mapRow(ResultSet resultSet, int i) throws SQLException {
        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeId(resultSet.getInt("recipe_id"));
        newRecipe.setRecipeName(resultSet.getString("name"));
        newRecipe.setRecipeDescription(resultSet.getString("description"));
        newRecipe.setImageRef(resultSet.getString("image_ref"));
        newRecipe.setDatePosted(resultSet.getDate("date_posted").toLocalDate());
        newRecipe.setCuisineId(resultSet.getInt("cuisine_id"));
        newRecipe.setUserId(resultSet.getInt("app_user_id"));
        return newRecipe;
    }
}
