package edu.school21.sockets.services;

import edu.school21.sockets.models.User;
import edu.school21.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Component("usersService")
public class UsersServiceImpl implements UsersService {
    private final UsersRepository repository;
    private final PasswordEncoder encoder;

    @Autowired
    public UsersServiceImpl(@Qualifier("UsersRepositoryImpl") UsersRepository repository, @Qualifier("bCryptPasswordEncoder") PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public void signUp(String name, String password) {
        List<User> userList = repository.findAll();
        long maxId = 0L;
        if (userList != null) {
            maxId = (long) userList.size();
        }
        String hashedPassword = encoder.encode(password);
        User user = new User(maxId, name, hashedPassword);
        repository.save(user);
    }

    @Override
    public User signIn(String name, String password) {
        List<User> userList = repository.findAll();
        for (User user : userList) {
            if (user.getName().equals(name) && encoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User findUserById(Long id) {
        return repository.findById(id).orElse(null);
    }
}
