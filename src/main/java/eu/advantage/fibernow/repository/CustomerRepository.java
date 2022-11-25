package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;

import javax.persistence.TypedQuery;
import java.util.List;

public class CustomerRepository extends GenericRepository <Customer, Long> {
    @Override
    public Customer findById(Long id) {
        return this.getEm().find(Customer.class, id);
    }

    @Override
    public List<Customer> findAll() {
        return this.getEm()//
                .createQuery("SELECT e FROM CUSTOMER e", Customer.class)//
                .getResultList();
    }

    @Override
    public TypedQuery<Customer> createQuery(String query) {
        return getEm().createQuery(query, Customer.class);
    }
}
