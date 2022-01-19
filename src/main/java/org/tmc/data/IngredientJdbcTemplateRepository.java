package org.tmc.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.tmc.data.mappers.IngredientMapper;
import org.tmc.models.Ingredient;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class IngredientJdbcTemplateRepository implements IngredientRepository {

    private final JdbcTemplate jdbcTemplate;

    public IngredientJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Ingredient> findAll() {
        final String sql = "select ingredient_id, name, quantity, recipe_id "
                + "from ingredient;";

        return jdbcTemplate.query(sql, new IngredientMapper());
    }

    @Override
    public List<Ingredient> findByRecipeId(int recipeId) {
        final String sql = "select ingredient_id, name, quantity, recipe_id "
                + "from ingredient "
                + "where recipe_id = ?;";

        return jdbcTemplate.query(sql, new IngredientMapper(), recipeId);
    }

    @Override
    public Ingredient findById(int ingredientId) {
        final String sql = "select ingredient_id, name, quantity, recipe_id "
                + "from ingredient "
                + "where ingredient_id = ?;";

        return jdbcTemplate.query(sql, new IngredientMapper(), ingredientId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public Ingredient add(Ingredient ingredient) {
        final String sql = "insert into ingredient (name, quantity, recipe_id)"
                + "values (?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, ingredient.getIngredientName());
            ps.setString(2, ingredient.getQuantity());
            ps.setInt(3, ingredient.getRecipeId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        ingredient.setIngredientId(keyHolder.getKey().intValue());
        return ingredient;
    }

    @Override
    public boolean update(Ingredient ingredient) {
        // Don't allow changing of recipe_id
        final String sql = "update ingredient set "
                + "name = ?, "
                + "quantity = ? "
                + "where ingredient_id = ?;";

        return jdbcTemplate.update(sql,
                ingredient.getIngredientName(),
                ingredient.getQuantity(),
                ingredient.getIngredientId()) > 0;
    }

    @Override
    public boolean deleteById(int ingredientId) {
        return jdbcTemplate.update(
                "delete from ingredient where ingredient_id = ?;", ingredientId) > 0;
    }

    @Override
    public boolean deleteByRecipeId(int recipeId) {
        return jdbcTemplate.update(
                "delete from ingredient where recipe_id = ?;", recipeId) > 0;
    }
}
