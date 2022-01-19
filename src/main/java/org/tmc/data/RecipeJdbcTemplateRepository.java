package org.tmc.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.tmc.data.mappers.RecipeMapper;
import org.tmc.models.Recipe;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class RecipeJdbcTemplateRepository implements RecipeRepository {

    private final JdbcTemplate jdbcTemplate;

    public RecipeJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Recipe> findAll() {
        final String sql = "select recipe_id, `name`, `description`, image_ref, date_posted, cuisine_id, app_user_id "
                + "from recipe limit 1000;";
        return jdbcTemplate.query(sql, new RecipeMapper());
    }

    @Override
    public List<Recipe> findAllByUserId(int userId) {

        final String sql = "select recipe_id, `name`, `description`, image_ref, date_posted, cuisine_id, app_user_id "
                + "from recipe where app_user_id = ?;";

        return jdbcTemplate.query(sql, new RecipeMapper(), userId);
    }

    @Override
    public List<Recipe> findAllByCuisineId(int cuisineId) {

        final String sql = "select recipe_id, `name`, `description`, image_ref, date_posted, cuisine_id, app_user_id "
                + "from recipe where cuisine_id = ?;";

        return jdbcTemplate.query(sql, new RecipeMapper(), cuisineId);
    }

    @Override
    @Transactional
    public Recipe findById(int recipeId) {

        final String sql = "select recipe_id, `name`, `description`, image_ref, date_posted, cuisine_id, app_user_id "
                + "from recipe where recipe_id = ?;";

        return jdbcTemplate.query(sql, new RecipeMapper(), recipeId).stream()
                .findFirst().orElse(null);
    }

    @Override
    public boolean findByUserIdAndRecipeName(int userId, String recipeName) {
        final String sql = "select count(*) > 0 existing "
                + "from recipe where app_user_id = ? and `name` = ?;";

        return jdbcTemplate.queryForObject(sql, Boolean.class, userId, recipeName);
    }

    @Override
    public Recipe add(Recipe toAdd) {

        final String sql = "insert into recipe (`name`, `description`, image_ref, date_posted, cuisine_id, app_user_id)"
                + " values (?,?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, toAdd.getRecipeName());
            ps.setString(2, toAdd.getRecipeDescription());
            ps.setString(3, toAdd.getImageRef());
            ps.setDate(4, Date.valueOf(toAdd.getDatePosted()));
            ps.setInt(5, toAdd.getCuisineId());
            ps.setInt(6, toAdd.getUserId());
            return ps;
        }, keyHolder);

        if (rowsAffected <= 0) {
            return null;
        }

        toAdd.setRecipeId(keyHolder.getKey().intValue());

        return toAdd;
    }

    @Override
    public boolean update(Recipe toUpdate) {

        final String sql = "update recipe set "
                + "`name` = ?, "
                + "`description` = ?, "
                + "image_ref = ?, "
                + "date_posted = ?, "
                + "cuisine_id = ?, "
                + "app_user_id = ? "
                + "where recipe_id = ?;";


        return jdbcTemplate.update(sql,
                toUpdate.getRecipeName(),
                toUpdate.getRecipeDescription(),
                toUpdate.getImageRef(),
                toUpdate.getDatePosted(),
                toUpdate.getCuisineId(),
                toUpdate.getUserId(),
                toUpdate.getRecipeId()) > 0;
    }

    @Override
    @Transactional
    public boolean deleteById(int recipeId) {
        jdbcTemplate.update("delete from ingredient where recipe_id = ?;", recipeId);
        return jdbcTemplate.update("delete from recipe where recipe_id = ?;", recipeId) > 0;
    }
}
