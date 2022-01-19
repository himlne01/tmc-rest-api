package org.tmc.data.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.tmc.models.UserData;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDataMapper implements RowMapper<UserData> {

    @Override
    public UserData mapRow(ResultSet resultSet, int i) throws SQLException {
        UserData userData = new UserData();
        userData.setUserId(resultSet.getInt("app_user_id"));
        userData.setUsername(resultSet.getString("username"));
        userData.setEmail(resultSet.getString("email"));
        userData.setUserDisabled(resultSet.getInt("disabled"));
        return userData;
    }
}
