package org.tmc.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.tmc.data.UserDataRepository;
import org.tmc.models.UserData;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserDataServiceTest {

    @Autowired
    UserDataService service;

    @MockBean
    UserDataRepository repository;

    @Test
    void shouldNotUpdateWhenInvalid() {
        UserData userData = makeUser();
        Result<UserData> actual = service.update(userData);
        assertEquals(ResultType.INVALID, actual.getType());

        userData = makeUser();
        userData.setUserId(1);
        userData.setUsername(" ");
        actual = service.update(userData);
        assertEquals(ResultType.INVALID, actual.getType());

        userData = makeUser();
        userData.setUserId(1);
        userData.setUserDisabled(100);
        actual = service.update(userData);
        assertEquals(ResultType.INVALID, actual.getType());
    }

    @Test
    void shouldUpdate() {
        UserData userData = makeUser();
        userData.setUserId(1);

        when(repository.update(userData)).thenReturn(true);

        Result<UserData> actual = service.update(userData);
        assertEquals(ResultType.SUCCESS, actual.getType());
    }

    UserData makeUser() {
        UserData user = new UserData();
        user.setUsername("Test");
        user.setEmail("test@dev.com");
        user.setUserDisabled(0);
        return user;
    }
}
