package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Chatroom;

import java.util.List;

public interface ChatroomsRepository {
    List<Chatroom> findAll();

    boolean save(Chatroom entity);
}
