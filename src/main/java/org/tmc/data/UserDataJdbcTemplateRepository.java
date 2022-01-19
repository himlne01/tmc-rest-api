package org.tmc.data;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.tmc.data.mappers.UserDataMapper;
import org.tmc.models.UserData;

import java.util.List;

@Repository
public class UserDataJdbcTemplateRepository implements UserDataRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserDataJdbcTemplateRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<UserData> findAll() {
        final String sql = "select app_user_id, username, email, disabled "
                + "from app_user;";

        return jdbcTemplate.query(sql, new UserDataMapper());
    }

    @Override
    public UserData findById(int userId) {
        final String sql = "select app_user_id, username, email, disabled "
                + "from app_user "
                + "where app_user_id = ?;";

        return jdbcTemplate.query(sql, new UserDataMapper(), userId).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public UserData findByUsername(String username) {
        final String sql = "select app_user_id, username, email, disabled "
                + "from app_user "
                + "where username = ?;";

        return jdbcTemplate.query(sql, new UserDataMapper(), username).stream()
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean update(UserData userData) {
        // No changing the disabled state
        // Only change the username and email
        final String sql = "update app_user set "
                + "username = ?, "
                + "email = ? "
                + "where app_user_id = ?;";

        return jdbcTemplate.update(sql,
                userData.getUsername(),
                userData.getEmail(),
                userData.getUserId()) > 0;
    }
}
