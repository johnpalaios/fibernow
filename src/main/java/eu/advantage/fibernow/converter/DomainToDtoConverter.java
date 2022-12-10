package eu.advantage.fibernow.converter;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;

public class DomainToDtoConverter {

    private DomainToDtoConverter() {}

    public static CustomerDto toDto(Customer customer) {
        if (customer == null) {
            return null;
        }
        CustomerDto dto = new CustomerDto();
        dto.setId(customer.getId());
        dto.setTin(customer.getTin());
        dto.setName(customer.getName());
        dto.setSurname(customer.getSurname());
        dto.setEmail(customer.getEmail());
        dto.setPhoneNumber(customer.getPhoneNumber());
        dto.setAddress(customer.getAddress());
        dto.setUsername(customer.getUsername());
        dto.setPassword(customer.getPassword());
        dto.setUserStatus(customer.getUserStatus());
        return dto;
    }

    public static TicketDto toDto(Ticket ticket) {
        if (ticket == null) {
            return null;
        }
        TicketDto dto = new TicketDto();
        dto.setId(ticket.getId());
        dto.setCustomerDto(toDto(ticket.getCustomer()));
        dto.setReceivedDate(ticket.getReceivedDate());
        dto.setScheduledDatetime(ticket.getScheduledDatetime());
        dto.setTicketStatus(ticket.getTicketStatus());
        dto.setType(ticket.getType());
        dto.setEstimatedCost(ticket.getEstimatedCost());
        dto.setAddress(ticket.getAddress());
        dto.setDescription(ticket.getDescription());
        return dto;
    }
}
