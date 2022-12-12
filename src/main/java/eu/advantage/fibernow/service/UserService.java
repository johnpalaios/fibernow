package eu.advantage.fibernow.service;

import eu.advantage.fibernow.model.AbstractUser;

public interface UserService<T extends AbstractUser> {
    T getUserByUsername(String username);
    T login(String username, String password);
}
