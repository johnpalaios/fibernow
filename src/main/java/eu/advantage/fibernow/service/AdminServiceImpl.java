package eu.advantage.fibernow.service;

import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.exception.ExceptionStatus;
import eu.advantage.fibernow.model.Admin;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.repository.AdminRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
public class AdminServiceImpl implements AdminService {

    @Inject
    AdminRepository repository;

    @Override
    @Transactional
    public Admin saveAdmin(Admin admin) {
        Long id = admin.getId();
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
}
