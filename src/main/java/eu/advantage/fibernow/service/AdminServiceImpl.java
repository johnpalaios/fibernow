package eu.advantage.fibernow.service;

import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.exception.ExceptionStatus;
import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.repository.AdminRepository;
import eu.advantage.fibernow.repository.GenericRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static eu.advantage.fibernow.exception.ExceptionStatus.BZ_ERROR_1010;
import static eu.advantage.fibernow.exception.ExceptionStatus.BZ_ERROR_3002;

@Stateless
@Slf4j
public class AdminServiceImpl extends AbstractUserService<Admin> implements AdminService {

    @Inject
    AdminRepository repository;
    @Inject
    private UserService<Admin> adminUserService;
    @Inject
    private UserService<Customer> customerUserService;

    @Override
    @Transactional
    public Admin saveAdmin(Admin admin) {
        Long id = admin.getId();
        if(checkIfUsernameExists(admin.getCredentials().getUsername(), id)) {
            throw new BusinessException(BZ_ERROR_3002, admin.getCredentials().getUsername());
        }
        if (id == null) {
            if (admin.getUserStatus() == null) {
                admin.setUserStatus(UserStatus.ACTIVE);
            }
            repository.create(admin);
        } else {
            Admin found = repository.findById(id);
            if (found == null) {
                throw new BusinessException(ExceptionStatus.BZ_ERROR_3001, id);
            }
            repository.update(admin);
        }
        log.info("Admin " + admin + " was created successfully");
        return admin;
    }

    @Override
    public Admin findAdmin(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<Admin> findAll() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public void deleteAdmin(Admin admin) {
        repository.delete(admin);
    }

    @Override
    public GenericRepository<Admin, Long> getRepository() {
        return repository;
    }

    private boolean checkIfUsernameExists(String username, Long id) {
        if(adminUserService.getUserByUsername(username) != null) return true;
        Customer customer = customerUserService.getUserByUsername(username);
        if(customer == null) return false;
        return !(customer.getId() == id);
    }
}
