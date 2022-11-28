package eu.advantage.fibernow.repository;

import eu.advantage.fibernow.model.Ticket;

public class TicketRepositoryImpl extends AbstractRepository<Ticket, Long> implements ITicketRepository {
    public TicketRepositoryImpl() {
        this.setPersistentClass(Ticket.class);
    }
}
