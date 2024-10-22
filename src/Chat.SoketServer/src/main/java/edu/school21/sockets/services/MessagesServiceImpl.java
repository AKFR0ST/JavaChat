package edu.school21.sockets.services;

import edu.school21.sockets.models.Message;
import edu.school21.sockets.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("messagesService")
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository repository;

    @Autowired
    public MessagesServiceImpl(@Qualifier("MessagesRepositoryImpl") MessagesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void sendMessage(Long sender, Long chatId, String message) {
        List<Message> messageList = repository.findAll();
        long maxId = 0L;
        if (messageList != null) {
            maxId = (long) messageList.size();
        }
        Date currentTime = new Date();
        repository.save(new Message(maxId + 1, sender, message, currentTime, chatId));
    }

    @Override
    public List<Message> getLastMessages(Long chatId) {
        return repository.getLastMessages(chatId);
    }
}
