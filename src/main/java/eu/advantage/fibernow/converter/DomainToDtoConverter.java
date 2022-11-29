package eu.advantage.fibernow.converter;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;

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

    public static TicketDto toDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        return TicketDto.builder()
                .id(ticket.getId())
                .customerDto(toDto(ticket.getCustomer()))
                .receivedDate(ticket.getReceivedDate())
                .scheduledDatetime(ticket.getScheduledDatetime())
                .status(ticket.getStatus())
                .type(ticket.getType())
                .estimatedCost(ticket.getEstimatedCost())
                .address(ticket.getAddress())
                .description(ticket.getDescription())
                .build();
    }
}
