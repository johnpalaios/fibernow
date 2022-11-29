package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Customer;

public interface CustomerService {
    CustomerDto saveCustomer(CustomerDto dto);
    CustomerDto findCustomer(Long id);
    CustomerDto searchCustomer(String email, String tid);
    CustomerDto deleteCustomer(Long id);
}
