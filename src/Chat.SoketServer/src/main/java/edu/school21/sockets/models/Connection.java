package edu.school21.sockets.models;

import java.io.BufferedWriter;
import java.util.StringJoiner;

public class Connection {
    Long userId;
    Long chatId;
    BufferedWriter out;

    public Connection(Long userId, Long chatId, BufferedWriter out) {
        this.userId = userId;
        this.chatId = chatId;
        this.out = out;
    }

    public Connection() {
        this.userId = 0L;
        this.chatId = 0L;
        this.out = null;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public BufferedWriter getOut() {
        return this.out;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(",", "{", "}");
        sj.add(userId.toString());
        sj.add(chatId.toString());
        return sj.toString();
    }

}
