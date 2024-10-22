package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;


@Component("MessagesRepositoryImpl")
public class MessagesRepositoryImpl implements MessagesRepository {

        private NamedParameterJdbcTemplate jdbcTemplate;

        public MessagesRepositoryImpl() {

        }

    @Autowired
    public void MessagesRepositoryJdbcTemplateImpl(
            @Qualifier("driverManagerDataSource") DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }


    @Override
    public List<Message> findAll() {
        String findAllQuery = "SELECT * FROM messages";
        List<Message> messages = jdbcTemplate.query(findAllQuery,
                new MessagesMapingColumnes());
        return messages.isEmpty() ? null : messages;
    }

    @Override
    public List<Message> getLastMessages(Long chatId) {
        String findLastQuery = "SELECT * FROM messages WHERE chatid = :chatid ORDER BY timestamp ASC LIMIT 30";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("chatid", chatId);
        List<Message> messages = jdbcTemplate.query(findLastQuery,
               params, new MessagesMapingColumnes());
        return messages.isEmpty() ? null : messages;
    }

    @Override
    public void save(Message entity) {
        String saveQuery = "INSERT INTO messages (id, sender, message, timestamp, chatid) VALUES (:id, :sender, :message, :timestamp, :chatid)";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", entity.getId());
        params.addValue("message", entity.getMessage());
        params.addValue("sender", entity.getSender());
        params.addValue("timestamp", entity.getDate());
        params.addValue("chatid", entity.getChatId());
        if (jdbcTemplate.update(saveQuery, params) == 0) {
            System.err.println("User wasn't saved with id = " + entity.getId());
        }
    }
}
