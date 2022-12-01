package eu.advantage.fibernow.model;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.Hibernate;
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
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TICKET")
public class Ticket implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    @ToString.Exclude
    private Customer customer;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    @Column(name = "receivedDate")
    private LocalDate receivedDate; // the date the ticket was received
    @Column(name = "scheduledDatetime")
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    @Column(name = "status")
    private TicketStatus status; // default : STANDBY
    @Column(name = "type")
    private TicketType type;
    @Column(name = "estimatedCost")
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
        return Objects.equals(getCustomer(), ticket.getCustomer()) && Objects.equals(getReceivedDate(), ticket.getReceivedDate()) && Objects.equals(getScheduledDatetime(), ticket.getScheduledDatetime()) && getStatus() == ticket.getStatus() && getType() == ticket.getType() && Objects.equals(getEstimatedCost(), ticket.getEstimatedCost()) && Objects.equals(getAddress(), ticket.getAddress()) && Objects.equals(getDescription(), ticket.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCustomer(), getReceivedDate(), getScheduledDatetime(), getStatus(), getType(), getEstimatedCost(), getAddress(), getDescription());
    }
}
