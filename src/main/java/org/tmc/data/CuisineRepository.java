package org.tmc.data;

import org.tmc.models.Cuisine;

import java.util.List;

public interface CuisineRepository {

    List<Cuisine> findAll();
}
