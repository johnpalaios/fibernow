package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.AdminDto;
import eu.advantage.fibernow.model.Admin;

public interface AdminService {
    Admin createAdmin(AdminDto dto);
    Admin findAdmin(Long id, String email);
    Admin updateAdmin(AdminDto dto);
    Admin deleteAdmin(AdminDto dto);

}
