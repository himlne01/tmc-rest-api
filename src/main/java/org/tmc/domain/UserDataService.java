package org.tmc.domain;

import org.springframework.stereotype.Service;
import org.tmc.data.UserDataRepository;
import org.tmc.models.UserData;

import java.util.List;

@Service
public class UserDataService {

    private final UserDataRepository repository;

    public UserDataService(UserDataRepository repository) {
        this.repository = repository;
    }

    public List<UserData> findAll() {
        return repository.findAll();
    }

    public UserData findById(int userId) {
        return repository.findById(userId);
    }

    public UserData findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public Result<UserData> update(UserData userData) {
        Result<UserData> result = validate(userData);
        if (!result.isSuccess()) {
            return result;
        }

        if (userData.getUserId() <= 0) {
            result.addMessage("User id must be set for update", ResultType.INVALID);
            return result;
        }

        if (!repository.update(userData)) {
            String msg = String.format("User Id: %s, not found", userData.getUserId());
            result.addMessage(msg, ResultType.NOT_FOUND);
        }

        return result;
    }

    private Result<UserData> validate(UserData userData) {
        Result<UserData> result = new Result<>();
        if (userData == null) {
            result.addMessage("User cannot be null", ResultType.INVALID);
            return result;
        }

        if (userData.getUsername() == null || userData.getUsername().isBlank()) {
            result.addMessage("Username cannot be blank", ResultType.INVALID);
        }

        // Email is nullable or not required

        if (userData.getUserDisabled() < 0 || userData.getUserDisabled() > 1) {
            result.addMessage("User disabling is a boolean", ResultType.INVALID);
        }

        return result;
    }
}
