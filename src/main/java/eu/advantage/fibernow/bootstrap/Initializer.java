package eu.advantage.fibernow.bootstrap;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.service.AdminService;
import eu.advantage.fibernow.service.CustomerService;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;
import jakarta.inject.Inject;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Startup
@Singleton
@Slf4j
public class Initializer {

    @Inject
    AdminService service;

    @PostConstruct
    public void init() {
        List<Admin> found = service.findAll();
        if (found.isEmpty()) {
            Admin admin = new Admin();
            admin.setUsername("admin");
            admin.setPassword("admin");
            admin.setUserStatus(UserStatus.ACTIVE);
            service.saveAdmin(admin);
        log.info("Admin " + admin + " was created successfully");
        }
        log.info("Admin account(s) already exist(s) in the DB.");
    }
}
