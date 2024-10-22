package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatroomsMapingColumnes implements RowMapper<Chatroom> {
    @Override
    public Chatroom mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Chatroom(
                rs.getLong("id"),
                rs.getString("chatname"));
    }
}
