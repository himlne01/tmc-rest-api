package org.tmc.domain;

import org.springframework.stereotype.Service;
import org.tmc.data.CuisineRepository;
import org.tmc.models.Cuisine;

import java.util.List;

@Service
public class CuisineService {

    private final CuisineRepository repository;

    public CuisineService(CuisineRepository repository) {
        this.repository = repository;
    }

    public List<Cuisine> findAll() {
        return repository.findAll();
    }
}
