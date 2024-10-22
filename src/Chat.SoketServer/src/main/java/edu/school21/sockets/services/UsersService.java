package edu.school21.sockets.services;

import edu.school21.sockets.models.User;

public interface UsersService {
    void signUp(String name, String password);

    User signIn(String name, String password);

    User findUserById(Long id);
}
