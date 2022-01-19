package org.tmc.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tmc.domain.Result;
import org.tmc.domain.UserDataService;
import org.tmc.models.UserData;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost:3000"})
@RequestMapping("/api/user")
public class UserDataController {

    private final UserDataService service;

    public UserDataController(UserDataService service) {
        this.service = service;
    }

    @GetMapping
    public List<UserData> findAll() {
        return service.findAll();
    }

    @GetMapping("/{userId}")
    public UserData findById(@PathVariable int userId) {
        return service.findById(userId);
    }

    @GetMapping("/name/{username}")
    public UserData findByUsername(@PathVariable String username) {
        return service.findByUsername(username);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> update(@PathVariable int userId, @RequestBody UserData userData) {
        if (userId != userData.getUserId()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Result<UserData> result = service.update(userData);
        if (result.isSuccess()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ErrorResponse.build(result);
    }
}
