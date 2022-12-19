package eu.advantage.fibernow.bootstrap;

import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.UserCredentials;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.service.AdminService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Startup
@Singleton
@Slf4j
public class Initializer {

    @Inject
    AdminService service;

    @PostConstruct
    public void init() {
        log.info("Initialization starts...");
        List<Admin> found = service.findAll();
        if (found.isEmpty()) {
            Admin admin = new Admin();
            UserCredentials credentials = new UserCredentials();
            credentials.setUsername("admin");
            credentials.setPassword("admin");
            admin.setCredentials(credentials);
            admin.setUserStatus(UserStatus.ACTIVE);
            service.saveAdmin(admin);
        } else {
            log.info("Admin account(s) already exist(s) in the DB.");
        }
    }
}
