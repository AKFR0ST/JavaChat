package edu.school21.sockets.models;

import java.util.StringJoiner;

public class User {
    private Long id;
    private String name;
    private String password;

    public User(Long id, String name, String password) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    public User() {
        this.id = 0L;
        this.name = "";
        this.password = "";
    }

    public long getId() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        StringJoiner joiner = new StringJoiner(",", "{", "}");
        joiner.add("id=" + id);
        joiner.add("name=" + name);
        joiner.add("password=" + password);
        return joiner.toString();
    }
}
