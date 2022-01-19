package org.tmc.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tmc.data.IngredientRepository;
import org.tmc.models.Ingredient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IngredientServiceTest {

    @Autowired
    IngredientService service;

    @MockBean
    IngredientRepository repository;

    @Test
    void shouldNotAddWhenInvalid() {
        // Test you cannot add an ingredient with a blank name
        Ingredient ingredient = makeIngredient();
        ingredient.setIngredientName(" ");
        Result<Ingredient> actual = service.add(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());

        // Test you cannot add an ingredient with a null quantity
        ingredient = makeIngredient();
        ingredient.setQuantity(null);
        actual = service.add(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());

        // Test you cannot add an ingredient with an invalid recipe id
        ingredient = makeIngredient();
        ingredient.setRecipeId(-1000);
        actual = service.add(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());

        // Test you cannot add a blank ingredient
        ingredient = new Ingredient();
        actual = service.add(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldAdd() {
        Ingredient ingredient = makeIngredient();
        Ingredient mockOut = makeIngredient();
        mockOut.setIngredientId(0);

        when(repository.add(ingredient)).thenReturn(mockOut);

        Result<Ingredient> actual = service.add(ingredient);
        assertEquals(ResultType.SUCCESS, actual.getType());
        assertEquals(mockOut, actual.getPayload());
    }

    @Test
    void shouldNotUpdateWhenInvalid() {
        Ingredient ingredient = makeIngredient();
        Result<Ingredient> actual = service.update(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());

        ingredient = makeIngredient();
        ingredient.setIngredientId(1);
        ingredient.setIngredientName(" ");
        actual = service.update(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());

        ingredient = makeIngredient();
        ingredient.setIngredientId(1);
        ingredient.setQuantity(null);
        actual = service.update(ingredient);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        Ingredient ingredient = makeIngredient();
        ingredient.setIngredientId(1);

        when(repository.update(ingredient)).thenReturn(true);

        Result<Ingredient> actual = service.update(ingredient);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    Ingredient makeIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("Test");
        ingredient.setQuantity("Test");
        ingredient.setRecipeId(1);
        return ingredient;
    }
}
