package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.CustomerRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static eu.advantage.fibernow.converter.DomainToDtoConverter.toDto;
import static eu.advantage.fibernow.converter.DtoToDomainConverter.*;
import static eu.advantage.fibernow.exception.ExceptionStatus.*;

@Stateless
public class CustomerServiceImpl implements CustomerService {

    @Inject
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDto saveCustomer(CustomerDto dto) throws BusinessException{
        Customer customer = toDomain(dto);
        if (customer.getUserStatus() == null) {
            customer.setUserStatus(UserStatus.ACTIVE);
        }
        List<CustomerDto> tinCustomers = customerRepository
                .searchByCriteria("tin", customer.getTin()).stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
        List<CustomerDto> emailCustomers = customerRepository
                .searchByCriteria("email", customer.getEmail()).stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
        if (customer.getId() == null) {
            if (!tinCustomers.isEmpty()) {
                throw new BusinessException(BZ_ERROR_1003, customer.getTin());
            }
            if (!emailCustomers.isEmpty()) {
                throw new BusinessException(BZ_ERROR_1004, customer.getEmail());
            }
            customerRepository.create(customer);
        }
        else {
            Customer found = customerRepository.findById(customer.getId());
            if (found == null) {
                throw new BusinessException(BZ_ERROR_1001, customer.getId());
            }
            if (!tinCustomers.isEmpty() && Objects.equals(tinCustomers.get(0).getId(), customer.getId())) {
                throw new BusinessException(BZ_ERROR_1005, customer.getTin());
            }
            if (!emailCustomers.isEmpty() && Objects.equals(emailCustomers.get(0).getId(), customer.getId())) {
                throw new BusinessException(BZ_ERROR_1006, customer.getEmail());
            }
            customerRepository.update(customer);
        }
        return toDto(customer);
    }

    @Override
    public CustomerDto findCustomer(Long id) throws BusinessException{
        Customer found = customerRepository.findById(id);
        if (found == null) {
            throw new BusinessException(BZ_ERROR_1001, id);
        }
        return toDto(found);
    }

    @Override
    public List<CustomerDto> searchCustomers(String email, String tin) throws BusinessException{
        if(tin != null) {
            List<CustomerDto> found = customerRepository
                    .searchByCriteria("tin", tin).stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());

            if (found.isEmpty()) {
                throw new BusinessException(BZ_ERROR_1008, tin);
            }
            return found;
        }
        if(email != null) {
            List<CustomerDto> found = customerRepository
                    .searchByCriteria("email", email).stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());

            if (found.isEmpty()) {
                throw new BusinessException(BZ_ERROR_1009, email);
            }
            return found;
        }
        List<CustomerDto> found =  customerRepository
                .findAll()
                .stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());

        if (found.isEmpty()) {
            throw new BusinessException(BZ_ERROR_1002);
        }
        return found;
    }

    @Override
    @Transactional
    public CustomerDto deleteCustomer(Long id) throws BusinessException{
        Customer found = customerRepository.findById(id);
        if (found == null) {
            throw new BusinessException(BZ_ERROR_1001, id);
        }

        Set<Ticket> tickets = found.getTickets();
        tickets.forEach(ticket -> {
            TicketStatus status = ticket.getTicketStatus();
            if ( status != TicketStatus.DELETED && status != TicketStatus.COMPLETED) {
                throw new BusinessException(BZ_ERROR_1007, id);
            }
        });

        //Soft Delete
        found.setUserStatus(UserStatus.DELETED);
        customerRepository.update(found);
        return toDto(found);
    }
}
