package eu.advantage.fibernow.service;

import eu.advantage.fibernow.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    Ticket saveTicket(Ticket dto);
    Ticket findTicket(Long ticketId);
    List<Ticket> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate);
    Ticket deleteTicket(Ticket dto);
}
