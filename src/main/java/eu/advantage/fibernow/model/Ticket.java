package eu.advantage.fibernow.model;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "TICKET")
public class Ticket extends AbstractEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "received_date")
    private LocalDate receivedDate; // the date the ticket was received
    @Column(name = "scheduled_datetime")
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus; // default : STANDBY
    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TicketType type;
    @Column(name = "estimated_cost")
    private BigDecimal estimatedCost;
    @Column(name = "address")
    private String address;
    @Column(name = "description")
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ticket ticket = (Ticket) o;
        return Objects.equals(getCustomer(), ticket.getCustomer()) &&
                Objects.equals(getReceivedDate(), ticket.getReceivedDate()) &&
                Objects.equals(getScheduledDatetime(), ticket.getScheduledDatetime()) &&
                getTicketStatus() == ticket.getTicketStatus() &&
                getType() == ticket.getType() &&
                Objects.equals(getEstimatedCost(), ticket.getEstimatedCost()) &&
                Objects.equals(getAddress(), ticket.getAddress()) &&
                Objects.equals(getDescription(), ticket.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(),
                getReceivedDate(),
                getScheduledDatetime(),
                getTicketStatus(),
                getType(),
                getEstimatedCost(),
                getAddress(),
                getDescription());
    }
}
