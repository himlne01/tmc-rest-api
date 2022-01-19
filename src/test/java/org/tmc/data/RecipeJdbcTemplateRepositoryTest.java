package org.tmc.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tmc.models.Recipe;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class RecipeJdbcTemplateRepositoryTest {

    @Autowired
    RecipeJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Recipe> recipeList = repository.findAll();
        assertNotNull(recipeList);
        assertTrue(recipeList.size() <= 6 && recipeList.size() >= 4);
    }

    @Test
    void shouldFindAllByUserId() {
        List<Recipe> recipes = repository.findAllByUserId(2);
        assertNotNull(recipes);
        assertEquals(2, recipes.size());
    }

    @Test
    void shouldFindAllByItalianId() {
        List<Recipe> recipes = repository.findAllByCuisineId(1);
        assertNotNull(recipes);
        assertEquals(3, recipes.size());
    }

    @Test
    void shouldAdd() {
        int before = repository.findAll().size();
        Recipe toAdd = makeRecipe();
        toAdd = repository.add(toAdd);
        assertEquals(before + 1, repository.findAll().size());
    }

    @Test
    void shouldFindByUserIdAndRecipeName() {
        Recipe recipe = makeRecipe();
        repository.add(recipe);

        assertTrue(repository.findByUserIdAndRecipeName(recipe.getUserId(), recipe.getRecipeName()));
    }

    @Test
    void shouldUpdate() {
        Recipe recipe = repository.findById(2);
        recipe.setRecipeName("Test");
        boolean isUpdated = repository.update(recipe);
        assertTrue(isUpdated);
        assertEquals("Test", repository.findById(2).getRecipeName());
    }

    @Test
    void shouldDeleteById() {
        assertTrue(repository.deleteById(3));
        assertFalse(repository.deleteById(3));
    }

    Recipe makeRecipe() {
        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeName("Test");
        newRecipe.setRecipeDescription("Test");
        newRecipe.setImageRef("Test");
        newRecipe.setDatePosted(LocalDate.now());
        newRecipe.setCuisineId(2);
        newRecipe.setUserId(3);
        return newRecipe;
    }
}