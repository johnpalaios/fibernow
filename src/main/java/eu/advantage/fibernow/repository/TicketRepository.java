package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;

import java.time.LocalDate;
import java.util.List;

public interface TicketRepository extends GenericRepository<Ticket, Long> {
    List<Ticket> findTicketsBetweenDates(LocalDate startDate, LocalDate endDate);
    List<Ticket> findTicketsByCustomer(Customer customer);
}
