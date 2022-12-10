package eu.advantage.fibernow.service;

import eu.advantage.fibernow.model.Admin;

import java.util.List;

public interface AdminService {
    Admin saveAdmin(Admin admin);
    Admin findAdmin(Long id);

    List<Admin> findAll();

    void deleteAdmin(Admin admin);

}
