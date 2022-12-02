package eu.advantage.fibernow.service;

import eu.advantage.fibernow.exception.BusinessException;
import eu.advantage.fibernow.model.Ticket;
import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.repository.ITicketRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import eu.advantage.fibernow.model.Customer;


import java.time.LocalDate;
import java.util.*;

import static eu.advantage.fibernow.exception.ExceptionStatus.*;
import static eu.advantage.fibernow.util.JPAHelper.*;
import static eu.advantage.fibernow.util.JPAHelper.closeEntityManager;

@Stateless
public class TicketServiceImpl implements TicketService{

    @Inject
    private ITicketRepository ticketRepository;
    @Inject
    private CustomerService customerService;

    @Override
    public Ticket saveTicket(Ticket ticket) {
        beginTransaction();
        try {
            if (ticket.getId() == null) {
                if(ticket.getReceivedDate() == null) {
                    ticket.setReceivedDate(LocalDate.now());
                }
                if(ticket.getStatus() == null) {
                    ticket.setStatus(TicketStatus.STANDBY);
                }
                ticketRepository.create(ticket);
            } else {
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
        return ticket;
    }

    @Override
    public Ticket findTicket(Long ticketId) {
        beginTransaction();
        Ticket ticket = null;
        try {
            ticket = ticketRepository.findById(ticketId);
            if (ticket == null) {
                throw new BusinessException(BZ_ERROR_2001, ticketId);
            }
            commitTransaction();
        } catch (Exception e) {
            rollbackTransaction();
            // rethrow exception
        } finally {
            closeEntityManager();
        }
        return ticket;
    }

    @Override
    public List<Ticket> searchTickets(Long customerId, LocalDate startDate, LocalDate endDate) {
        List<Ticket> ticketList = new ArrayList<>();
        beginTransaction();
        try {
            if(customerId != null) {
                Customer customer = customerService.findCustomer(customerId);
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
//            e.getMessage();
        } finally {
            closeEntityManager();
        }
        return ticketList;
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate) {
        if(startDate != null && endDate != null) {
            return ticketRepository.findTicketsBetweenDates(startDate, endDate);
        } else if(startDate != null){
            Map<String, Object> criteriaMap = new HashMap<>();
            criteriaMap.put("receivedDate", startDate);
            return ticketRepository.findByCriteria(criteriaMap);
        } else {
            throw new BusinessException(BZ_ERROR_2003);
        }
    }

    private List<Ticket> filterByDates(LocalDate startDate, LocalDate endDate, Set<Ticket> ticketSet) {
        List<Ticket> ticketList = new ArrayList<>();
        if(startDate != null && endDate != null) {
            if(endDate.isBefore(startDate)) {
                throw new BusinessException(BZ_ERROR_2002, startDate.toString(), endDate.toString());
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


    @Override
    public Ticket deleteTicket(Ticket ticket) {
        beginTransaction();
        Ticket found;
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
        return ticket;
    }
}
