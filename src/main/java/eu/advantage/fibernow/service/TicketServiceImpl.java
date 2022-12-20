package eu.advantage.fibernow.service;

import eu.advantage.fibernow.converter.DomainToDtoConverter;
import eu.advantage.fibernow.dto.CustomerDto;
import eu.advantage.fibernow.dto.TicketDto;
import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.UserStatus;
import eu.advantage.fibernow.repository.TicketRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static eu.advantage.fibernow.converter.DomainToDtoConverter.*;
import static eu.advantage.fibernow.converter.DtoToDomainConverter.*;
import static eu.advantage.fibernow.exception.ExceptionStatus.*;

@Stateless
@Slf4j
public class TicketServiceImpl implements TicketService{

    @Inject
    private TicketRepository ticketRepository;

    @Inject
    private CustomerService customerService;

    @Override
    @Transactional
    public TicketDto saveTicket(TicketDto dto) throws BusinessException{
        Ticket ticket = toDomain(dto);
        log.info("Called saveTicket() with TicketDto : {}", dto);
        CustomerDto customerDto;
        try {
            customerDto = customerService.findCustomer(dto.getCustomerId());
        } catch(Exception e) {
            throw new BusinessException(BZ_ERROR_1001, dto.getCustomerId());
        }
        Customer customer = toDomain(customerDto);
        if(customer.getUserStatus() == UserStatus.DELETED || customer.getUserStatus() == UserStatus.INACTIVE) {
            throw new BusinessException(BZ_ERROR_2004, ticket.getId(), customer.getId());
        }
        ticket.setCustomer(customer);
        if (ticket.getId() == null) {
            if(ticket.getReceivedDate() == null) {
                ticket.setReceivedDate(LocalDate.now());
            }
            if(ticket.getTicketStatus() == null) {
                ticket.setTicketStatus(TicketStatus.STANDBY);
            }
            ticketRepository.create(ticket);
            log.info("Ticket {} - Created.", ticket);
        } else {
            Ticket found = ticketRepository.findById(ticket.getId());
            if (found == null) {
                log.error("Trying to update Ticket : {} - This Ticket doesn't exist.", ticket);
                throw new BusinessException(BZ_ERROR_2001, ticket.getId());
            }
            ticketRepository.update(ticket);
            log.info("Ticket : {} - Updated.", ticket);
        }
//        addTicketToCustomer(ticket);
        return toDto(ticket);
    }

    @Override
    public TicketDto findTicket(Long ticketId) throws BusinessException{
        log.info("Called findTicket() with Ticket Id : {}", ticketId);
        Ticket ticket = ticketRepository.findById(ticketId);
        if (ticket == null) {
            log.error("Ticket with Id : {} - Not found.",ticketId);
            throw new BusinessException(BZ_ERROR_2001, ticketId);
        }
        log.info("Ticket {} - Found.", ticket);
        return toDto(ticket);
    }

    @Override
    public List<TicketDto> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) throws BusinessException{
        log.info("Called searchTickets() with Customer Id = {}, StartDate = {} and EndDate = {}.",
                customerId, startDate, endDate);
        if (customerId == null) {
            log.info("Going to search for Tickets only with Start Date and End Date - Customer Id is null.");
            return filterByDates(startDate, endDate)
                    .stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());
        }
        Customer customer;
        try {
            customer = toDomain(customerService.findCustomer(customerId));
        } catch(Exception e) {
            throw new BusinessException(BZ_ERROR_1001, customerId);
        }
        List<Ticket> tickets = new ArrayList<>(customer.getTickets());
        tickets.forEach(ticket -> ticket.setCustomer(customer));
        if(startDate == null) {
            log.info("Going to search for Tickets only with Customer Id = {} - startDate is null.", customerId);
            return tickets
                    .stream()
                    .map(DomainToDtoConverter::toDto)
                    .collect(Collectors.toList());
        }
        log.info("Going to call filterByDates with startDate = {}, endDate = {} and tickets = {}.", startDate, endDate, tickets);
        return filterByDates(startDate, endDate, tickets)
                .stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<TicketDto> findTop10TicketsAfterDate(LocalDateTime dateTime) throws BusinessException {
        if(dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        log.info("Trying to find the First Pending Tickets after {}", dateTime);
        List<Ticket> tickets = ticketRepository.findTop10TicketsOrderedByDateTime(dateTime);
        return tickets
                .stream()
                .map(DomainToDtoConverter::toDto)
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public TicketDto deleteTicket(Long id) throws BusinessException{
        log.info("Called deleteTicket() with Id = {}", id);
        Ticket found;
        found = ticketRepository.findById(id);
        if (found == null) {
            log.error("Failed to delete Ticket with Id = {} - This ticket does not exist", id);
            throw new BusinessException(BZ_ERROR_2001, id);
        }
        if(found.getTicketStatus() == TicketStatus.DELETED) {
            throw new BusinessException(BZ_ERROR_2005,id);
        }
        //Soft Delete
        found.setTicketStatus(TicketStatus.DELETED);
        ticketRepository.update(found);
        log.info("Deleted : Ticket {}", found);
        return toDto(found);
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate) throws BusinessException{
        log.info("Called filterByDates() with startDate = {} and endDate = {}", startDate, endDate);
        if(startDate != null && endDate != null) {
            log.info("findTicketsBetweenDates() is called with startDate = {} and endDate = {}", startDate, endDate);
            return ticketRepository.findTicketsBetweenDates(startDate, endDate);
        } else if(startDate != null){
            log.info("findTicketsBetweenDates() is called with startDate = endDate = {}", startDate);
            return ticketRepository.findTicketsBetweenDates(startDate, startDate);
        } else {
            log.debug("In filterByDates() - Error: startDate is null");
            throw new BusinessException(BZ_ERROR_2003);
        }
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate, List<Ticket> tickets) throws BusinessException{
        log.info("Called filterByDates() with startDate = {} , endDate = {} and tickets = {}", startDate, endDate, tickets);
        if (startDate == null) {
            return tickets;
        }
        if(endDate != null) {
            if(endDate.isBefore(startDate)) {
                log.error("In filterByDates - Error : endDate : {} is before startDate : {}", endDate, startDate);
                throw new BusinessException(BZ_ERROR_2002, startDate.toString(), endDate.toString());
            }
            return tickets.stream()
                    .filter(ticket -> (ticket.getReceivedDate().isAfter(startDate) || ticket.getReceivedDate().isEqual(startDate))
                            && (ticket.getReceivedDate().isBefore(endDate) || ticket.getReceivedDate().isEqual(endDate)))
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
        if (customerTickets == null) {
            customerTickets = new HashSet<>();
        }
        customerTickets.add(toDto(ticket));
        customer.setTickets(customerTickets);
        customerService.saveCustomer(customer);
    }
}
