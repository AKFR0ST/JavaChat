package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessagesRepository {
    List<Message> findAll();

    void save(Message entity);

    List<Message> getLastMessages(Long chatId);
}
