package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.CustomerDto;

public interface CustomerService {
    CustomerDto saveCustomer(CustomerDto dto);
    CustomerDto searchCustomer(String email, String tid);
    CustomerDto deleteCustomer(CustomerDto dto);
}
