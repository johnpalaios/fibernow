package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Admin;

public class AdminRepositoryImpl extends AbstractRepository<Admin, Long> implements IAdminRepository {
    public AdminRepositoryImpl() {
        this.setPersistentClass(Admin.class);
    }
}
