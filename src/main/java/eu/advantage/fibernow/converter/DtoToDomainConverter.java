package eu.advantage.fibernow.converter;

import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;

public class DtoToDomainConverter {
    public static Customer toDomain(CustomerDto dto) {
        if (dto == null) {
            return null;
        }
        Customer domain = new Customer();
        domain.setId(dto.getId());
        domain.setTin(dto.getTin());
        domain.setName(dto.getName());
        domain.setSurname(dto.getSurname());
        domain.setEmail(dto.getEmail());
        domain.setPhoneNumber(dto.getPhoneNumber());
        domain.setAddress(dto.getAddress());
        domain.setUsername(dto.getUsername());
        domain.setPassword(dto.getPassword());
        domain.setUserStatus(dto.getUserStatus());
        return domain;
    }

    public static Ticket toDomain(TicketDto dto) {
        if (dto == null) {
            return null;
        }
        Ticket ticket = new Ticket();
        ticket.setId(dto.getId());
        ticket.setCustomer(toDomain(dto.getCustomerDto()));
        ticket.setReceivedDate(dto.getReceivedDate());
        ticket.setScheduledDatetime(dto.getScheduledDatetime());
        ticket.setTicketStatus(dto.getTicketStatus());
        ticket.setType(dto.getType());
        ticket.setEstimatedCost(dto.getEstimatedCost());
        ticket.setAddress(dto.getAddress());
        ticket.setDescription(dto.getDescription());
        return ticket;
    }
}
