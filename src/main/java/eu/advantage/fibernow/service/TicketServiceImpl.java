package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.ITicketRepository;
import jakarta.inject.Inject;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.repository.ICustomerRepository;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static eu.advantage.fibernow.exception.ExceptionStatus.*;
import static eu.advantage.fibernow.util.JPAHelper.*;
import static eu.advantage.fibernow.util.JPAHelper.closeEntityManager;

public class TicketServiceImpl implements TicketService{

    @Inject
    private ITicketRepository ticketRepository;
    @Inject
    private ICustomerRepository customerRepository;

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

    //if findTicket returns null -- the ticket with this ticketId doesn't exist
    @Override
    public TicketDto findTicket(Long ticketId) {
        beginTransaction();
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(ticketId);
            if (ticket == null) {
                throw new BusinessException(BZ_ERROR_1011, ticketId);
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
    public List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<Ticket> ticketList = new ArrayList<>();
        beginTransaction();
        try {
            if(customerId != null) {
                Customer customer = customerRepository.findById(customerId);
                Set<Ticket> ticketSet = customer.getTickets();
                if(startDate != null) {
                    ticketList = filterByDates(startDate, endDate, ticketSet);
                } else {
                    ticketList.addAll(ticketSet);
                }
            } else {
                ticketList = filterByDates(startDate, endDate);
            }
            commitTransaction();
        } catch(Exception e) {
            rollbackTransaction();
        } finally {
            closeEntityManager();
        }
        return transformTicketCollectionToTicketDtoList(ticketList);
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate) {
        if(startDate != null && endDate != null) {
            return ticketRepository.findTicketsBetweenDates(startDate, endDate);
        } else if(startDate != null){
            Map<String, Object> criteriaMap = new HashMap<>();
            criteriaMap.put("receivedDate", startDate);
            return ticketRepository.findByCriteria(criteriaMap);
        } else {
            throw new BusinessException(BZ_ERROR_1013);
        }
    }
    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate, Set<Ticket> ticketSet) {
        List<Ticket> ticketList = new ArrayList<>();
        if(startDate != null && endDate != null) {
            if(endDate.isBefore(startDate)) {
                throw new BusinessException(BZ_ERROR_1012, startDate.toString(), endDate.toString());
            }
            for(Ticket ticket : ticketSet) {
                LocalDate date = ticket.getReceivedDate();
                if(date.isAfter(startDate) && date.isBefore(endDate)) {
                    ticketList.add(ticket);
                }
            }
        } else if(startDate != null) {
            for (Ticket ticket : ticketSet) {
                LocalDate date = ticket.getReceivedDate();
                if (date.equals(startDate)) {
                    ticketList.add(ticket);
                }
            }
        }
        return ticketList;
    }

    private <T extends Collection<Ticket>> List<TicketDto> transformTicketCollectionToTicketDtoList(T ticketCollection) {
        return ticketCollection.stream().map(DomainToDtoConverter::toDto).collect(Collectors.toList());
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
