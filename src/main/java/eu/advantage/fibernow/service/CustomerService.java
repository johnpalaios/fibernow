package eu.advantage.fibernow.service;

import eu.advantage.fibernow.model.Customer;

public interface CustomerService {
    Customer saveCustomer(Customer dto);
    Customer findCustomer(Long id);
    Customer searchCustomer(String email, String tid);
    Customer deleteCustomer(Long id);
}
