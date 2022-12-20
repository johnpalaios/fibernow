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
        log.info("Try to Login with Username : {}", username);
        T user = getUserByUsername(username);
        if (user != null) {
            String pass = user.getCredentials().getPassword();
            log.info("Try to check if given password : {} is the same as the password in the DB : {}", password, pass);
            if (!pass.equals(password)) {
                log.info("This user.getId() : {}", user.getId());
                throw new BusinessException(ExceptionStatus.BZ_ERROR_4002, username);
            }
        }
        return user;
    }
    private Long getIdByUsername(String username) {
        log.info("GetIdByUsername({})", username);
        try{
            UserCredentials credentials = userRepository.getCredentialsByUsername(username);
            return credentials.getId();
        } catch (Exception e){
            throw new BusinessException(ExceptionStatus.BZ_ERROR_4001, username);
        }
    }

}
