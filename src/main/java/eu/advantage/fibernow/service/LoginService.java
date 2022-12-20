package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.UserCredentialsDto;

public interface LoginService {
    String getUserType(UserCredentialsDto userCredentialsDto);
}
