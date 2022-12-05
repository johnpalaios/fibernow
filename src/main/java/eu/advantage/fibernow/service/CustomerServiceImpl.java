package eu.advantage.fibernow.service;


import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.enums.Status;
import eu.advantage.fibernow.repository.ICustomerRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;

import static eu.advantage.fibernow.exception.ExceptionStatus.*;
import static eu.advantage.fibernow.util.JPAHelper.*;

@Stateless
public class CustomerServiceImpl implements CustomerService {

    @Inject
    private ICustomerRepository customerRepository;

    @Override
    public Customer saveCustomer(Customer customer) {
        beginTransaction();
        try {
            if (customer.getId() == null) {
                if(customer.getStatus() == null) {
                    customer.setStatus(Status.ACTIVE);
                }
                customerRepository.create(customer);
            }
            else {
                Customer found = customerRepository.findById(customer.getId());
                if (found == null) {
                    throw new BusinessException(BZ_ERROR_1001, customer.getId());
                }
                customerRepository.update(customer);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return customer;
    }

    @Override
    public Customer findCustomer(Long id) {
        beginTransaction();
        Customer found = null;
        try {
            found = customerRepository.findById(id);
            if (found == null) {
                throw new BusinessException(BZ_ERROR_1001, id);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return found;
    }

    @Override
    public Customer searchCustomer(String email, String tid) {
        beginTransaction();
        try {
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return null;
    }

    @Override
    public Customer deleteCustomer(Long id) {
        beginTransaction();
        Customer found = null;
        try {
            found = customerRepository.findById(id);
            if (found == null) {
                throw new BusinessException(BZ_ERROR_1001, id);
            }

            //Check if the Customer has open Tickets

            //Soft Delete
            found.setStatus(Status.DELETED);
            customerRepository.update(found);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return found;
    }
}
