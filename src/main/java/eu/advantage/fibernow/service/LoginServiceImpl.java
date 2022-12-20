package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.UserCredentialsDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.Customer;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import static eu.advantage.fibernow.exception.ExceptionStatus.BZ_ERROR_4003;


@Slf4j
public class LoginServiceImpl implements LoginService{
    @Inject
    UserService<Admin> adminUserService;
    @Inject
    UserService<Customer> customerUserService;

    @Override
    public String getUserType(UserCredentialsDto userCredentialsDto) {
        String username = userCredentialsDto.getUsername();
        String password = userCredentialsDto.getPassword();
        log.info("Trying to login with username : {} and password : {}", username, password);
        if (adminUserService.login(username, password) != null) {
            log.info("User : {} is Admin", userCredentialsDto);
            return "ADMIN";
        } else if (customerUserService.login(username, password) != null) {
            log.info("User : {} is Customer", userCredentialsDto);
            return "CUSTOMER";
        } else {
            log.info("User : {} is neither a Customer nor an Admin", userCredentialsDto);
            throw new BusinessException(BZ_ERROR_4003,username);
        }
    }
}

