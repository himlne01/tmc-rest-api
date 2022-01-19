package org.tmc.data;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.tmc.models.UserData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserDataJdbcTemplateRepositoryTest {

    @Autowired
    UserDataJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setUp() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<UserData> users = repository.findAll();
        assertNotNull(users);

        assertTrue(users.size() >= 3 && users.size() <= 4);
    }

    @Test
    void shouldFindSpencerById() {
        UserData spencer = repository.findById(1);
        assertNotNull(spencer);
        assertEquals(1, spencer.getUserId());
        assertEquals("Spencer", spencer.getUsername());
        assertEquals("spencer@dev.com", spencer.getEmail());
    }

    @Test
    void shouldFindSpencerByUsername() {
        UserData spencer = repository.findByUsername("Spencer");
        assertNotNull(spencer);
        assertEquals(1, spencer.getUserId());
        assertEquals("spencer@dev.com", spencer.getEmail());
    }

    @Test
    void shouldUpdate() {
        UserData user = new UserData();
        user.setUsername("Test");
        user.setEmail("test@dev.com");
        user.setUserId(2);
        assertTrue(repository.update(user));

        UserData actual = repository.findById(2);
        assertEquals("Test", actual.getUsername());
        assertEquals("test@dev.com", actual.getEmail());

        user.setUserId(1000);
        assertFalse(repository.update(user));
    }
}
