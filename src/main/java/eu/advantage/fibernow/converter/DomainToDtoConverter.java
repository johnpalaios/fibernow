package eu.advantage.fibernow.converter;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.model.Customer;

public class DomainToDtoConverter {
    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerDto.builder()
                .id(customer.getId())
                .tin(customer.getTin())
                .name(customer.getName())
                .surname(customer.getSurname())
                .email(customer.getEmail())
                .phoneNumber(customer.getPhoneNumber())
                .address(customer.getAddress())
                .username(customer.getUsername())
                .password(customer.getPassword())
                .status(customer.getStatus())
                .build();
    }
}
