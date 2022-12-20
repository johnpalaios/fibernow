package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Customer;
import eu.advantage.fibernow.model.Ticket;

import eu.advantage.fibernow.model.enums.TicketStatus;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class TicketRepositoryImpl extends AbstractRepository<Ticket, Long> implements TicketRepository {

    @Inject
    EntityManager em;

    public TicketRepositoryImpl() {
        this.setPersistentClass(Ticket.class);
    }

    @Override
    public List<Ticket> findTicketsBetweenDates(LocalDate startDate, LocalDate endDate) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
        Root<Ticket> root = criteriaQuery.from(Ticket.class);

        criteriaQuery.select(root).where(builder.between(root.get("receivedDate"), startDate, endDate));

        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Ticket> findTicketsByCustomer(Customer customer) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Ticket> criteriaQuery = builder.createQuery(Ticket.class);
        Root<Ticket> root = criteriaQuery.from(Ticket.class);
        criteriaQuery.select(root).where(builder.equal(root.get("customer"), customer));
        return em.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<Ticket> findTop10TicketsOrderedByDateTime(LocalDateTime dateTime) {
        return em.createQuery("SELECT t FROM Ticket t WHERE t.scheduledDatetime > :dateTime AND t.ticketStatus = :pendingStatus ORDER BY t.scheduledDatetime ASC",Ticket.class)
                .setParameter("dateTime",dateTime)
                .setParameter("pendingStatus", TicketStatus.PENDING)
                .setMaxResults(10)
                .getResultList();
    }


}
