package edu.school21.sockets.services;

import edu.school21.sockets.models.Chatroom;

import java.util.List;

public interface ChatroomsService {
    boolean addChatroom(String name);

    List<Chatroom> getChatrooms();
}
