package org.tmc.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tmc.domain.RecipeService;
import org.tmc.domain.Result;
import org.tmc.models.Recipe;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin(origins = {"*"})
@RequestMapping("/api/recipe")
public class RecipeController {

    private final RecipeService service;

    public RecipeController(RecipeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Recipe> findAll() {
        return service.findAll();
    }

    @GetMapping("/user/{userId}")
    public List<Recipe> findAllByUserId(@PathVariable int userId) {
        return service.findAllByUserId(userId);
    }

    @GetMapping("/cuisine/{cuisineId}")
    public List<Recipe> findAllByCuisineId(@PathVariable int cuisineId) {
        return service.findAllByCuisineId(cuisineId);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Object> findById(@PathVariable int recipeId) {

        Result<Recipe> result = service.findById(recipeId);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.OK);
        }

        return ErrorResponse.build(result);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody Recipe toAdd, Principal principal) {
        String username = principal.getName();
        toAdd.setUsername(username);

        Result<Recipe> result = service.add(toAdd);

        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getPayload(), HttpStatus.CREATED);
        }

        return ErrorResponse.build(result);
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<Object> update(@PathVariable int recipeId, @RequestBody Recipe toUpdate) {

        if (recipeId != toUpdate.getRecipeId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<Recipe> result = service.update(toUpdate);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<Void> deleteById(@PathVariable int recipeId) {

        if (service.deleteById(recipeId)) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
