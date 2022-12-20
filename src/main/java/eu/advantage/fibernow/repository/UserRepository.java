package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.UserCredentials;

import java.util.List;

public interface UserRepository {
    List<UserCredentials> getCredentialsByUsername(String username);
}
