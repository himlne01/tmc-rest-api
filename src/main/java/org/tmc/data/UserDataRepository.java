package org.tmc.data;

import org.tmc.models.UserData;

import java.util.List;

public interface UserDataRepository {

    List<UserData> findAll();

    UserData findById(int userId);

    UserData findByUsername(String username);

    boolean update(UserData userData);
}
