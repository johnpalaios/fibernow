package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.ITicketRepository;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

import static eu.advantage.fibernow.exception.ExceptionStatus.BZ_ERROR_1001;
import static eu.advantage.fibernow.util.JPAHelper.*;
import static eu.advantage.fibernow.util.JPAHelper.closeEntityManager;

public class TicketServiceImpl implements TicketService{

    @Inject
    private ITicketRepository ticketRepository;
    @Override
    public TicketDto saveTicket(TicketDto dto) {
        Ticket ticket = DtoToDomainConverter.toDomain(dto);
        beginTransaction();
        try {
            if (ticket.getId() == null) {
                ticketRepository.create(ticket);
            }
            else {
                Ticket found = ticketRepository.findById(ticket.getId());
                if (found == null) {
                    throw new BusinessException(BZ_ERROR_1001, ticket.getId());
                }
                ticketRepository.update(ticket);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return DomainToDtoConverter.toDto(ticket);
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
        Ticket ticket = DtoToDomainConverter.toDomain(dto);
        beginTransaction();
        Ticket found = null;
        try {
            found = ticketRepository.findById(ticket.getId());
            if (found == null) {
                throw new BusinessException(BZ_ERROR_1001, ticket.getId());
            }

            //Check if the Customer has open Tickets

            //Soft Delete
            found.setStatus(TicketStatus.DELETED);
            ticketRepository.update(found);
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return DomainToDtoConverter.toDto(found);
    }
}
