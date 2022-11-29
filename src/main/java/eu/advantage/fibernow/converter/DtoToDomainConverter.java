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
        return Customer.builder()
                .id(dto.getId())
                .tin(dto.getTin())
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .address(dto.getAddress())
                .username(dto.getUsername())
                .password(dto.getPassword())
                .status(dto.getStatus())
                .build();
    }

    public static Ticket toDomain(TicketDto dto) {
        if (dto == null) {
            return null;
        }
        return Ticket.builder()
                .id(dto.getId())
                .customer(toDomain(dto.getCustomerDto()))
                .receivedDate(dto.getReceivedDate())
                .scheduledDatetime(dto.getScheduledDatetime())
                .status(dto.getStatus())
                .type(dto.getType())
                .estimatedCost(dto.getEstimatedCost())
                .address(dto.getAddress())
                .description(dto.getDescription())
                .build();
    }
}
