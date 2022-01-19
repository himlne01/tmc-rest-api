package org.tmc.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tmc.data.IngredientRepository;
import org.tmc.data.RecipeRepository;
import org.tmc.models.Ingredient;
import org.tmc.models.Recipe;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RecipeServiceTest {

    @Autowired
    RecipeService service;

    @MockBean
    RecipeRepository repository;

    @MockBean
    IngredientRepository ingredientRepository;

    @Test
    void add() {
        Recipe theAdd = makeRecipe();
        theAdd.setIngredientsList(makeIngredients());
        Recipe mock = makeRecipe();
        mock.setIngredientsList(makeIngredients());
        mock.setRecipeId(0);

        when(ingredientRepository.add(makeIngredients().get(0))).thenReturn(makeIngredients().get(0));
        when(ingredientRepository.add(makeIngredients().get(1))).thenReturn(makeIngredients().get(1));
        when(repository.add(theAdd)).thenReturn(mock);

        Result<Recipe> actual = service.add(theAdd);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mock, actual.getPayload());
    }

    @Test
    void shouldNotAddDuplicateRecipe() {
        Recipe recipe = makeRecipe();
        recipe.setIngredientsList(makeIngredients());

        when(repository.findByUserIdAndRecipeName(makeRecipe().getUserId(), makeRecipe().getRecipeName())).thenReturn(true);

        Result<Recipe> actual = service.add(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldNotAdd() {
        // blank
        Recipe recipe = makeRecipe();
        recipe.setRecipeName(" ");
        recipe.setRecipeDescription(" ");
        recipe.setImageRef(" ");
        recipe.setCuisineId(-1);
        Result<Recipe> actual = service.add(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
        assertEquals(5, actual.getMessages().size());

        // null
        recipe = makeRecipe();
        recipe.setRecipeName(null);
        recipe.setRecipeDescription(null);
        recipe.setImageRef(null);
        recipe.setIngredientsList(null);
        actual = service.add(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
        assertEquals(4, actual.getMessages().size());

        // null recipe
        recipe = new Recipe();
        actual = service.add(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Recipe theUpdated = makeRecipe();
        theUpdated.setRecipeId(2);
        theUpdated.setIngredientsList(makeIngredients());

        when(repository.update(theUpdated)).thenReturn(true);

        Result<Recipe> actual = service.update(theUpdated);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    @Test
    void shouldNotUpdate() {
        // No id
        Recipe recipe = makeRecipe();
        recipe.setIngredientsList(makeIngredients());
        Result<Recipe> actual = service.update(recipe);
        assertEquals("RecipeId must be set for `update` operation", actual.getMessages().get(0));
//        assertEquals(ResultType.NOT_FOUND, actual.getType());

        // blank
        recipe = makeRecipe();
        recipe.setRecipeId(2);
        recipe.setRecipeName(" ");
        recipe.setRecipeDescription(" ");
        recipe.setImageRef(" ");
        recipe.setCuisineId(-1);
        actual = service.update(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
        assertEquals(5, actual.getMessages().size());

        // null
        recipe = makeRecipe();
        recipe.setRecipeId(2);
        recipe.setRecipeName(null);
        recipe.setRecipeDescription(null);
        recipe.setImageRef(null);
        recipe.setIngredientsList(null);
        actual = service.update(recipe);
        assertEquals(ResultType.INVALID, actual.getType());
        assertEquals(4, actual.getMessages().size());
    }

    Recipe makeRecipe() {
        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeName("Test");
        newRecipe.setRecipeDescription("Test");
        newRecipe.setImageRef("Test");
        newRecipe.setDatePosted(LocalDate.now());
        newRecipe.setCuisineId(2);
        newRecipe.setUserId(2);
        return newRecipe;
    }

    List<Ingredient> makeIngredients() {
        Ingredient ingredientOne = new Ingredient();
        ingredientOne.setIngredientName("Testing");
        ingredientOne.setQuantity("1 Test");

        Ingredient ingredientTwo = new Ingredient();
        ingredientTwo.setIngredientName("Tester");
        ingredientTwo.setQuantity("2 Test");

        List<Ingredient> madeIngredients = new ArrayList<>();
        madeIngredients.add(ingredientOne);
        madeIngredients.add(ingredientTwo);

        return madeIngredients;
    }
}