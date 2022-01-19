package org.tmc.domain;

import org.springframework.stereotype.Service;
import org.tmc.data.IngredientRepository;
import org.tmc.models.Ingredient;

import java.util.List;

@Service
public class IngredientService {

    private final IngredientRepository repository;

    public IngredientService(IngredientRepository repository) {
        this.repository = repository;
    }

    public List<Ingredient> findAll() {
        return repository.findAll();
    }

    public List<Ingredient> findByRecipeId(int recipeId) {
        return repository.findByRecipeId(recipeId);
    }

    public Ingredient findById(int ingredientId) {
        return repository.findById(ingredientId);
    }

    public Result<Ingredient> add(Ingredient ingredient) {
        Result<Ingredient> result = validate(ingredient);
        if (!result.isSuccess()) {
            return result;
        }

        if (ingredient.getIngredientId() != 0) {
            result.addMessage("Ingredient Id cannot be set for add operation", ResultType.INVALID);
            return result;
        }

        ingredient = repository.add(ingredient);
        result.setPayload(ingredient);
        return result;
    }

    public Result<Ingredient> update(Ingredient ingredient) {
        Result<Ingredient> result = validate(ingredient);
        if (!result.isSuccess()) {
            return result;
        }

        if (ingredient.getIngredientId() <= 0) {
            result.addMessage("Ingredient Id must be set for update", ResultType.INVALID);
            return result;
        }

        if (!repository.update(ingredient)) {
            String msg = String.format("Ingredient Id: %s, not found", ingredient.getIngredientId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    public boolean deleteById(int ingredientId) {
        return repository.deleteById(ingredientId);
    }

    private Result<Ingredient> validate(Ingredient ingredient) {
        Result<Ingredient> result = new Result<>();
        if (ingredient == null) {
            result.addMessage("Ingredient cannot be null", ResultType.INVALID);
            return result;
        }

        if (ingredient.getIngredientName() == null || ingredient.getIngredientName().isBlank()) {
            result.addMessage("Ingredient name is required", ResultType.INVALID);
        }

        if (ingredient.getQuantity() == null || ingredient.getQuantity().isBlank()) {
            result.addMessage("Ingredient quantity is required", ResultType.INVALID);
        }

        if (ingredient.getRecipeId() <= 0) {
            result.addMessage("Recipe Id cannot be negative", ResultType.INVALID);
        }

        return result;
    }
}
