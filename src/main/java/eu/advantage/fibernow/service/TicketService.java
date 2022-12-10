package eu.advantage.fibernow.service;

import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface TicketService {
    TicketDto saveTicket(TicketDto dto) throws BusinessException;
    TicketDto findTicket(Long ticketId) throws BusinessException;
    List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) throws BusinessException;
    TicketDto deleteTicket(Long id) throws BusinessException;
}
