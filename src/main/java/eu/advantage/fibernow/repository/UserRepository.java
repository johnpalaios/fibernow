package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.UserCredentials;

public interface UserRepository {
    UserCredentials getCredentialsByUsername(String username);
}
