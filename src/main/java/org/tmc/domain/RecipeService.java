package org.tmc.domain;

import org.springframework.stereotype.Service;
import org.tmc.data.IngredientRepository;
import org.tmc.data.RecipeRepository;
import org.tmc.models.Ingredient;
import org.tmc.data.UserDataRepository;
import org.tmc.models.Recipe;
import org.tmc.models.UserData;

import java.time.LocalDate;
import java.util.List;

@Service
public class RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final UserDataRepository userRepository;

    public RecipeService(RecipeRepository recipeRepository, UserDataRepository userRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<Recipe> findAll() {
        List<Recipe> all = recipeRepository.findAll();
        for(Recipe recipe : all) {
            recipe = hydrate(recipe);
        }

        return all;
    }

    public List<Recipe> findAllByUserId(int userId) {
        List<Recipe> all = recipeRepository.findAllByUserId(userId);
        for(Recipe recipe : all) {
            recipe = hydrate(recipe);
        }

        return all;
    }

    public List<Recipe> findAllByCuisineId(int cuisineId) {
        List<Recipe> all = recipeRepository.findAllByCuisineId(cuisineId);
        for(Recipe recipe : all) {
            recipe = hydrate(recipe);
        }

        return all;
    }

    public List<Recipe> findAllByRecipeName(String recipeName) {
        List<Recipe> all = recipeRepository.findAllByRecipeName(recipeName);
        for(Recipe recipe : all) {
            recipe = hydrate(recipe);
        }

        return all;
    }

    public Result<Recipe> findById(int recipeId) {
        Result<Recipe> result = new Result<>();
        Recipe possibleNull = recipeRepository.findById(recipeId);

        if (possibleNull == null) {
            result.addMessage("recipeId not found", ResultType.NOT_FOUND);
            return result;
        }

        List<Ingredient> ingredientList = ingredientRepository.findByRecipeId(recipeId);
        possibleNull.setIngredientsList(ingredientList);

        possibleNull = hydrate(possibleNull);
        result.setPayload(possibleNull);
        return result;

    }

    public Result<Recipe> add(Recipe toAdd) {
        Result<Recipe> result = validate(toAdd);

        if (!result.isSuccess()) {
            return result;
        }

        if (toAdd.getRecipeId() != 0) {
            result.addMessage("RecipeId cannot be set for `add` operation", ResultType.INVALID);
            return result;
        }

        UserData userData = userRepository.findByUsername(toAdd.getUsername());
        toAdd.setUserId(userData.getUserId());

        if (recipeRepository.findByUserIdAndRecipeName(toAdd.getUserId(), toAdd.getRecipeName())) {
            result.addMessage("This recipe already exists", ResultType.INVALID);
            return result;
        }

        toAdd.setDatePosted(LocalDate.now());
        toAdd = recipeRepository.add(toAdd);
      
        // Ingredients not added to the model
        for (int i = 0; i < toAdd.getIngredientsList().size(); i++) {
            toAdd.getIngredientsList().get(i).setRecipeId(toAdd.getRecipeId());
            Ingredient added = ingredientRepository.add(toAdd.getIngredientsList().get(i));
            toAdd.getIngredientsList().set(i, added);
        }

        result.setPayload(toAdd);
        return result;
    }

    public Result<Recipe> update(Recipe toUpdate) {
        Result<Recipe> result = validate(toUpdate);

        if (!result.isSuccess()) {
            return result;
        }

        if (toUpdate.getRecipeId() <= 0) {
            result.addMessage("RecipeId must be set for `update` operation", ResultType.INVALID);
            return result;
        }

        if (!recipeRepository.update(toUpdate)) {
            String message = String.format("RecipeId %s not found", toUpdate.getRecipeId());
            result.addMessage(message, ResultType.NOT_FOUND);
        } else {
            ingredientRepository.deleteByRecipeId(toUpdate.getRecipeId());

            toUpdate.getIngredientsList().stream().forEach(ingredient -> {
                ingredient.setRecipeId(toUpdate.getRecipeId());
                ingredientRepository.add(ingredient);
            });
        }

        return result;
    }

    public boolean deleteById(int recipeId) {
        return recipeRepository.deleteById(recipeId);
    }

    private Result<Recipe> validate(Recipe toValidate) {

        Result<Recipe> result = new Result<>();

        if (toValidate == null) {
            result.addMessage("recipe cannot be null", ResultType.INVALID);
            return result;
        }

        if (toValidate.getRecipeName() == null || toValidate.getRecipeName().isBlank()) {
            result.addMessage("recipe name is required", ResultType.INVALID);
        }

        if (toValidate.getRecipeDescription() == null || toValidate.getRecipeDescription().isBlank()) {
            result.addMessage("recipe description is required", ResultType.INVALID);
        }

        if (toValidate.getImageRef() == null || toValidate.getImageRef().isBlank()) {
            result.addMessage("image reference is required", ResultType.INVALID);
        }

        if (toValidate.getCuisineId() > 26 || toValidate.getCuisineId() < 0) {
            result.addMessage("cuisine is required", ResultType.INVALID);
        }

        if (toValidate.getIngredientsList() == null || toValidate.getIngredientsList().isEmpty()) {
            result.addMessage("ingredients are required", ResultType.INVALID);
        }

        return result;
    }

    private Recipe hydrate(Recipe toHydrate) {
         UserData user =  userRepository.findById(toHydrate.getUserId());
         toHydrate.setUsername(user.getUsername());
         return toHydrate; // is hydrated
    }
}
