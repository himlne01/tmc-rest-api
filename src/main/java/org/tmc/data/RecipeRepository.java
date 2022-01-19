package org.tmc.data;

import org.springframework.transaction.annotation.Transactional;
import org.tmc.models.Recipe;

import java.util.List;

public interface RecipeRepository {

    List<Recipe> findAll();

    List<Recipe> findAllByUserId(int userId);

    List<Recipe> findAllByCuisineId(int cuisineId);

    Recipe findById(int recipeId);

    boolean findByUserIdAndRecipeName(int userId, String recipeName);

    Recipe add(Recipe toAdd);

    boolean update(Recipe toUpdate);

    boolean deleteById(int recipeId);
}
