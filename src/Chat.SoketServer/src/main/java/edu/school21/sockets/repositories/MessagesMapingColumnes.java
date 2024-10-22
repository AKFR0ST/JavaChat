package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MessagesMapingColumnes implements RowMapper<Message> {
    @Override
    public Message mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Message(
                rs.getLong("id"),
                rs.getLong("sender"),
                rs.getString("message"),
                rs.getDate("timestamp"),
                rs.getLong("chatid"));
    }
}
