package org.tmc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.tmc.domain.IngredientService;
import org.tmc.domain.Result;
import org.tmc.models.Ingredient;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/ingredient")
public class IngredientController {

    private final IngredientService service;

    public IngredientController(IngredientService service) {
        this.service = service;
    }

    @GetMapping
    public List<Ingredient> findAll() {
        return service.findAll();
    }

    @GetMapping("/recipe/{recipeId}")
    public List<Ingredient> findByRecipeId(@PathVariable int recipeId) {
        return service.findByRecipeId(recipeId);
    }

    @GetMapping("/{ingredientId}")
    public Ingredient findById(@PathVariable int ingredientId) {
        return service.findById(ingredientId);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Ingredient ingredient) {
        Result<Ingredient> result = service.add(ingredient);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }
        return ErrorResponse.build(result);
    }

    @PutMapping("/{ingredientId}")
    public ResponseEntity<Object> update(@PathVariable int ingredientId, @RequestBody Ingredient ingredient) {
        if (ingredientId != ingredient.getIngredientId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Ingredient> result = service.update(ingredient);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{ingredientId}")
    public ResponseEntity<Void> deleteById(@PathVariable int ingredientId) {
        if (service.deleteById(ingredientId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
