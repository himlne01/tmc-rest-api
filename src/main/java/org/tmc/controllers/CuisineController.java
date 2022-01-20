package org.tmc.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.tmc.domain.CuisineService;
import org.tmc.models.Cuisine;

import java.util.List;

@RestController
//@CrossOrigin(origins = {"*"})
@RequestMapping("/api/cuisine")
public class CuisineController {

    private final CuisineService service;

    public CuisineController(CuisineService service) {
        this.service = service;
    }

    @GetMapping
    public List<Cuisine> findAll() {
        return service.findAll();
    }
}
