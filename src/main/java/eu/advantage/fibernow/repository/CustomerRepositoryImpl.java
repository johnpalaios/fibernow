package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;
import jakarta.persistence.TypedQuery;

import static eu.advantage.fibernow.util.JPAHelper.getEntityManager;

public class CustomerRepositoryImpl extends AbstractRepository<Customer, Long> implements CustomerRepository {
    public CustomerRepositoryImpl() {
        this.setPersistentClass(Customer.class);
    }

    @Override
    public Customer findByTin(String tin) {
        TypedQuery<Customer> q = getEntityManager().createQuery("SELECT c FROM Customer c WHERE c.tin = :tin", Customer.class);
        q.setParameter("tin", tin);
        return q.getSingleResult();
    }

    @Override
    public Customer findByEmail(String email) {
        TypedQuery<Customer> q = getEntityManager().createQuery("SELECT c FROM Customer c JOIN c.email e WHERE e = :email", Customer.class);
        q.setParameter("email", email);
        return q.getSingleResult();
    }
}
