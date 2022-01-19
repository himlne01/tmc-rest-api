package org.tmc.data;

import org.tmc.models.Ingredient;

import java.util.List;

public interface IngredientRepository {

    List<Ingredient> findAll();

    List<Ingredient> findByRecipeId(int recipeId);

    Ingredient findById(int ingredientId);

    Ingredient add(Ingredient ingredient);

    boolean update(Ingredient ingredient);

    boolean deleteById(int ingredientId);

    boolean deleteByRecipeId(int recipeId);
}
