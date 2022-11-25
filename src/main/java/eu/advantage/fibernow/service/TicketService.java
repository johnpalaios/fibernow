package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.TicketDto;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    TicketDto createTicket(TicketDto dto);
    TicketDto updateTicket(TicketDto dto);
    TicketDto searchTicketById(Long ticketId);
    List<TicketDto> searchTickets(Long customerId);
    List<TicketDto> searchTickets(LocalDate date);
    List<TicketDto> searchTickets(LocalDate startDate, LocalDate endDate);
    TicketDto deleteTicket(TicketDto dto);
}
