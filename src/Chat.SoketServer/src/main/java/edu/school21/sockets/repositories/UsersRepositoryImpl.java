package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Component("UsersRepositoryImpl")
public class UsersRepositoryImpl implements UsersRepository {
    private NamedParameterJdbcTemplate jdbcTemplate;

    public UsersRepositoryImpl() {

    }

    @Autowired
    public void UsersRepositoryJdbcTemplateImpl(
            @Qualifier("driverManagerDataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<User> findAll() {
        String findAllQuery = "SELECT id, username, password FROM users";
        List<User> users = jdbcTemplate.query(findAllQuery,
                new UsersMapingColumnes());
        return users.isEmpty() ? null : users;
    }

    @Override
    public Optional<User> findById(Long id) {
        String findByIdQuery = "SELECT * FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id);
        User user = jdbcTemplate.query(findByIdQuery, params, new UsersMapingColumnes()).stream().findAny().orElse(null);
        return Optional.ofNullable(user);
    }

    @Override
    public void update(User entity) {
        String updateQuery = "UPDATE users SET password = :password WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", entity.getId());
        if (jdbcTemplate.update(updateQuery, params) == 0) {
            System.err.println("User wasn't updated with id = " + entity.getId());
        }
    }

    @Override
    public void save(User entity) {
        String saveQuery = "INSERT INTO users (id, username, password) VALUES (:id, :username, :password)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", entity.getId());
        params.addValue("username", entity.getName());
        params.addValue("password", entity.getPassword());
        if (jdbcTemplate.update(saveQuery, params) == 0) {
            System.err.println("User wasn't saved with id = " + entity.getId());
        }
    }

    @Override
    public void delete(Long id) {
        String updateQuery = "DELETE FROM users WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        if (jdbcTemplate.update(updateQuery, params) == 0) {
            System.err.println("User not found with id = " + id);
        }
    }

}
