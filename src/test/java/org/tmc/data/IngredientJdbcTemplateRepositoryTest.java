package org.tmc.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tmc.models.Ingredient;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class IngredientJdbcTemplateRepositoryTest {

    @Autowired
    IngredientJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Ingredient> ingredients = repository.findAll();
        assertNotNull(ingredients);

        assertTrue(ingredients.size() >= 30 && ingredients.size() <= 32);
    }

    @Test
    void shouldFindAllIngredientsForRecipe() {
        List<Ingredient> ingredients = repository.findByRecipeId(4);
        assertNotNull(ingredients);

        assertTrue(ingredients.size() >= 3 && ingredients.size() <= 4);
    }

    @Test
    void shouldFindSalmon() {
        Ingredient salmon = repository.findById(18);
        assertNotNull(salmon);
        assertEquals(18, salmon.getIngredientId());
        assertEquals("Raw Salmon", salmon.getIngredientName());
    }

    @Test
    void shouldAddIngredient() {
        Ingredient ingredient = makeIngredient();
        Ingredient actual = repository.add(ingredient);
        assertNotNull(actual);
        assertEquals(32, actual.getIngredientId());
    }

    @Test
    void shouldUpdate() {
        Ingredient ingredient = makeIngredient();
        ingredient.setIngredientId(20); //Update existing ingredient with ID 20
        assertTrue(repository.update(ingredient));

        Ingredient actual = repository.findById(20);
        assertEquals("Test", actual.getIngredientName());
        assertEquals("Test", actual.getQuantity());

        ingredient.setIngredientId(1000); //Do not update nonexistent ingredient
        assertFalse(repository.update(ingredient));
    }

    @Test
    void shouldDelete() {
        assertTrue(repository.deleteById(13));
        assertFalse(repository.deleteById(13));
    }

    Ingredient makeIngredient() {
        Ingredient ingredient = new Ingredient();
        ingredient.setIngredientName("Test");
        ingredient.setQuantity("Test");
        ingredient.setRecipeId(4);
        return ingredient;
    }
}
