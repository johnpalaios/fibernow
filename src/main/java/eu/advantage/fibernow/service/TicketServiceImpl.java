package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.TicketRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static eu.advantage.fibernow.converter.DomainToDtoConverter.*;
import static eu.advantage.fibernow.converter.DtoToDomainConverter.*;
import static eu.advantage.fibernow.exception.ExceptionStatus.*;

@Stateless
public class TicketServiceImpl implements TicketService{

    @Inject
    private TicketRepository ticketRepository;

    @Inject
    private CustomerService customerService;

    @Override
    @Transactional
    public TicketDto saveTicket(TicketDto dto) {
        Ticket ticket = toDomain(dto);
        if (ticket.getId() == null) {
            if(ticket.getReceivedDate() == null) {
                ticket.setReceivedDate(LocalDate.now());
            }
            if(ticket.getTicketStatus() == null) {
                ticket.setTicketStatus(TicketStatus.STANDBY);
            }
            ticketRepository.create(ticket);
        } else {
            Ticket found = ticketRepository.findById(ticket.getId());
            if (found == null) {
                throw new BusinessException(BZ_ERROR_1001, ticket.getId());
            }
            ticketRepository.update(ticket);
        }
        addTicketToCustomer(ticket);
        return toDto(ticket);
    }

    @Override
    public TicketDto findTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new BusinessException(BZ_ERROR_2001, ticketId);
        }
        return toDto(ticket);
    }

    @Override
    public List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<Ticket> ticketList = new ArrayList<>();
        if(customerId != null) {
            CustomerDto customer = customerService.findCustomer(customerId);
            Set<Ticket> ticketSet = customer
                    .getTickets()
                    .stream()
                    .map(DtoToDomainConverter::toDomain)
                    .collect(Collectors.toSet());
            if(startDate != null) {
                ticketList = filterByDates(startDate, endDate, ticketSet);
            } else {
                ticketList.addAll(ticketSet);
            }
        } else {
            ticketList = filterByDates(startDate, endDate);
        }
        return ticketList
                .stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketDto deleteTicket(Long id) {
        Ticket found;
        found = ticketRepository.findById(id);
        if (found == null) {
            throw new BusinessException(BZ_ERROR_1001, id);
        }
        //Soft Delete
        found.setTicketStatus(TicketStatus.DELETED);
        ticketRepository.update(found);
        return toDto(found);
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate) {
        if(startDate != null && endDate != null) {
            return ticketRepository.findTicketsBetweenDates(startDate, endDate);
        } else if(startDate != null){
            return ticketRepository.findTicketsBetweenDates(startDate, startDate);
        } else {
            throw new BusinessException(BZ_ERROR_2003);
        }
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate, Set<Ticket> ticketSet) {
        List<Ticket> ticketList = new ArrayList<>();
        if (startDate == null) {
            return new ArrayList<>(ticketSet);
        }
        if(endDate != null) {
            if(endDate.isBefore(startDate)) {
                throw new BusinessException(BZ_ERROR_2002, startDate.toString(), endDate.toString());
            }
            ticketSet.forEach(ticket -> {
                LocalDate date = ticket.getReceivedDate();
                if(date.isAfter(startDate) && date.isBefore(endDate)) {
                    ticketList.add(ticket);
                }
            });
        } else {
            ticketSet.forEach(ticket -> {
                if (ticket.getReceivedDate().equals(startDate)) {
                    ticketList.add(ticket);
                }
            });
        }
        return ticketList;
    }

    private void addTicketToCustomer(Ticket ticket) throws BusinessException{
        Long customerId = ticket.getCustomer().getId();
        CustomerDto customer = customerService.findCustomer(customerId);
        Set<TicketDto> customerTickets = customer.getTickets();
        customerTickets.add(toDto(ticket));
        customer.setTickets(customerTickets);
        customerService.saveCustomer(customer);
    }
}
