package eu.advantage.fibernow.service;

import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.exception.ExceptionStatus;
import eu.advantage.fibernow.model.AbstractUser;
import eu.advantage.fibernow.model.UserCredentials;
import eu.advantage.fibernow.repository.GenericRepository;
import eu.advantage.fibernow.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

@Stateless
@Slf4j
public abstract class AbstractUserService<T extends AbstractUser> implements UserService<T>{

    public abstract GenericRepository<T,Long> getRepository();
    @Inject
    private UserRepository userRepository;

    @Override
    public T getUserByUsername(String username) {
        Long id = getIdByUsername(username);
        if (id != null) {
            return getRepository().findById(id);
        }
        return null;
    }

    @Override
    public T login(String username, String password) {
        log.info("Try to Login with Username : {} ", username);
        T user = getUserByUsername(username);
        if (user != null) {
            String pass = user.getCredentials().getPassword();
            if (!pass.equals(password)) {
                throw new BusinessException(ExceptionStatus.BZ_ERROR_4002, user.getId());
            }
        }
        return user;
    }

    private Long getIdByUsername(String username) {
        UserCredentials credentials = userRepository.getCredentialsByUsername(username);
        if (credentials == null) {
            throw new BusinessException(ExceptionStatus.BZ_ERROR_4001, username);
        }
        return credentials.getId();
    }
}
