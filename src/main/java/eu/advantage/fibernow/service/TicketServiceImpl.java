package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.converter.DtoToDomainConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Customer;
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
    public TicketDto saveTicket(TicketDto dto) throws BusinessException{
        Ticket ticket = toDomain(dto);
        Customer customer = toDomain(customerService.findCustomer(dto.getCustomerId()));
        ticket.setCustomer(customer);
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
                throw new BusinessException(BZ_ERROR_2001, ticket.getId());
            }
            ticketRepository.update(ticket);
        }
        addTicketToCustomer(ticket);
        return toDto(ticket);
    }

    @Override
    public TicketDto findTicket(Long ticketId) throws BusinessException{
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            throw new BusinessException(BZ_ERROR_2001, ticketId);
        }
        return toDto(ticket);
    }

    @Override
    public List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) throws BusinessException{
        if (customerId == null) {
            return filterByDates(startDate, endDate)
                    .stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());
        }
        Customer customer = toDomain(customerService.findCustomer(customerId));
        List<Ticket> tickets = new ArrayList<>(customer.getTickets());
        if(startDate == null) {
            return tickets
                    .stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());
        }
        tickets = filterByDates(startDate, endDate, tickets);
        return tickets
                .stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TicketDto deleteTicket(Long id) throws BusinessException{
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

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate) throws BusinessException{
        if(startDate != null && endDate != null) {
            return ticketRepository.findTicketsBetweenDates(startDate, endDate);
        } else if(startDate != null){
            return ticketRepository.findTicketsBetweenDates(startDate, startDate);
        } else {
            throw new BusinessException(BZ_ERROR_2003);
        }
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate, List<Ticket> tickets) throws BusinessException{
        if (startDate == null) {
            return tickets;
        }
        if(endDate != null) {
            if(endDate.isBefore(startDate)) {
                throw new BusinessException(BZ_ERROR_2002, startDate.toString(), endDate.toString());
            }
            return tickets.stream()
                    .filter(ticket -> ticket.getReceivedDate().isAfter(startDate) &&
                            ticket.getReceivedDate().isBefore(endDate))
                    .collect(Collectors.toList());
        } else {
            return tickets.stream()
                    .filter(ticket -> ticket.getReceivedDate().equals(startDate))
                    .collect(Collectors.toList());
        }
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
