package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Customer;

public interface CustomerService {
    Customer saveCustomer(CustomerDto dto);
    Customer findCustomer(Long id);
    Customer searchCustomer(String email, String tid);
    Customer deleteCustomer(CustomerDto dto);
}
