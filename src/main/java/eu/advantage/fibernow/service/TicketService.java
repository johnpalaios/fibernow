package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    TicketDto saveTicket(TicketDto dto);
    TicketDto findTicket(Long ticketId);
    List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate);
    TicketDto deleteTicket(TicketDto dto);
}
