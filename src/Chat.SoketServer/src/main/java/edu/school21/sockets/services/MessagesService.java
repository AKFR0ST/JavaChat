package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;

import java.util.List;

public interface MessagesService {
    void sendMessage(Long sender, Long chatId, String message);

    List<Message> getLastMessages(Long chatId);
}
