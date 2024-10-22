package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component("ChatroomsRepositoryImpl")
public class ChatroomsRepositoryImpl implements ChatroomsRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;

    public ChatroomsRepositoryImpl() {

    }

    @Autowired
    public void ChatroomsRepositoryJdbcTemplateImpl(
            @Qualifier("driverManagerDataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<Chatroom> findAll() {
        String findAllQuery = "SELECT * FROM chatrooms";
        List<Chatroom> chatrooms = jdbcTemplate.query(findAllQuery,
                new ChatroomsMapingColumnes());
        return chatrooms.isEmpty() ? null : chatrooms;
    }

    @Override
    public boolean save(Chatroom entity) {
        boolean result = true;
        String saveQuery = "INSERT INTO chatrooms (id, chatname) VALUES (:id, :name)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", entity.getId());
        params.addValue("name", entity.getName());
        if (jdbcTemplate.update(saveQuery, params) == 0) {
            System.err.println("User wasn't saved with id = " + entity.getId());
            result = false;
        }
        return result;

    }
}
