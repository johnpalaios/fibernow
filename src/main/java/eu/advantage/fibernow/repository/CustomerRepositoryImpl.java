package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;

public class CustomerRepositoryImpl extends AbstractRepository<Customer, Long> implements CustomerRepository {
    public CustomerRepositoryImpl() {
        this.setPersistentClass(Customer.class);
    }
}
