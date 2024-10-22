package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.repositories.ChatroomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component("chatroomsService")
public class ChatroomsServiceImpl implements ChatroomsService {
    private final ChatroomsRepository repository;


    @Autowired
    public ChatroomsServiceImpl(@Qualifier("ChatroomsRepositoryImpl") ChatroomsRepository repository) {
        this.repository = repository;
    }


    @Override
    public boolean addChatroom(String name) {
        List<Chatroom> chatroomList = repository.findAll();
        long maxId = 0L;
        if (chatroomList != null) {
            maxId = (long) chatroomList.size();
        }
        Date currentTime = new Date();
        return repository.save(new Chatroom(maxId + 1, name));
    }

    @Override
    public List<Chatroom> getChatrooms() {
        return repository.findAll();
    }

}

