package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Customer;

import java.util.List;

public interface CustomerService extends UserService<Customer> {
    CustomerDto saveCustomer(CustomerDto dto) throws BusinessException;
    CustomerDto findCustomer(Long id) throws BusinessException;
    CustomerDto findCustomerByUsername(String username);

    List<CustomerDto> searchCustomers(String email, String tin) throws BusinessException;
    CustomerDto deleteCustomer(Long id) throws BusinessException;

}
