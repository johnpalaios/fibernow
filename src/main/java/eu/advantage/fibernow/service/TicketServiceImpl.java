package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.TicketDto;

import java.time.LocalDate;
import java.util.List;

public class TicketServiceImpl implements TicketService{

    @Override
    public TicketDto saveTicket(TicketDto dto) {
        return null;
    }

    @Override
    public TicketDto findTicket(Long ticketId) {
        return null;
    }

    @Override
    public List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public TicketDto deleteTicket(TicketDto dto) {
        return null;
    }
}
