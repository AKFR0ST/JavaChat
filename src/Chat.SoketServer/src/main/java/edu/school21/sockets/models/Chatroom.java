package edu.school21.sockets.models;

import java.util.StringJoiner;

public class Chatroom {
    private Long id;
    private String name;

    public Chatroom(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Chatroom() {
        this.id = 0L;
        this.name = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        StringJoiner sj = new StringJoiner(", ", "{", "}");
        sj.add("id=" + id);
        sj.add("name=" + name);
        return sj.toString();
    }
}
