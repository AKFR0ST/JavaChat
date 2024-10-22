package edu.school21.sockets.models;

import java.util.Date;
import java.util.StringJoiner;

public class Message {
    private Long id;
    private Long sender;
    private String message;
    private Date date;
    private Long chatId;

    public Message() {
        this.id = 0L;
        this.sender = 0L;
        this.message = "";
        this.date = new Date();
        this.chatId = 0L;
    }

    public Message(Long id, Long sender, String message, Date date, Long chatId) {
        this.id = id;
        this.sender = sender;
        this.message = message;
        this.date = date;
        this.chatId = chatId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSender() {
        return sender;
    }

    public void setSender(Long sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        sj.add("id=" + id);
        sj.add("sender=" + sender);
        sj.add("message=" + message);
        sj.add("date=" + date);
        return sj.toString();
    }
}
