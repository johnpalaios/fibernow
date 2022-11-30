package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Ticket;
import jakarta.ejb.Local;

import java.time.LocalDate;
import java.util.List;

public interface ITicketRepository extends IGenericRepository<Ticket, Long>{
    List<Ticket> findTicketsBetweenDates(LocalDate startDate, LocalDate endDate);
}
