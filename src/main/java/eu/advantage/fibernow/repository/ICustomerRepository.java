package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;

public interface ICustomerRepository extends IGenericRepository<Customer, Long> {
    Customer findByTin(String tin);
    Customer findByEmail(String email);
}
