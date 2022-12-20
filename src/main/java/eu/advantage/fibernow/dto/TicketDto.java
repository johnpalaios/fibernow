package eu.advantage.fibernow.dto;

import eu.advantage.fibernow.model.enums.TicketStatus;
import eu.advantage.fibernow.model.enums.TicketType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class TicketDto  implements Serializable {
    private Long id;
    @NotNull
    private Long customerId;
    @PastOrPresent
    private LocalDate receivedDate; // the date the ticket was received
    @NotNull
    @FutureOrPresent
    private LocalDateTime scheduledDatetime; // date and time of the scheduled action
    private TicketStatus ticketStatus; // default : STANDBY
    @NotNull
    private TicketType type;
    @PositiveOrZero
    private BigDecimal estimatedCost;
    @NotBlank
    private String address;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketDto ticketDto = (TicketDto) o;
        return Objects.equals(getReceivedDate(),
                ticketDto.getReceivedDate()) &&
                Objects.equals(getScheduledDatetime(),
                        ticketDto.getScheduledDatetime())
                && getTicketStatus() == ticketDto.getTicketStatus()
                && getType() == ticketDto.getType()
                && Objects.equals(getEstimatedCost(),
                ticketDto.getEstimatedCost())
                && Objects.equals(getAddress(), ticketDto.getAddress()) && Objects.equals(getDescription(), ticketDto.getDescription());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getReceivedDate(),
                getScheduledDatetime(),
                getTicketStatus(),
                getType(),
                getEstimatedCost(),
                getAddress(),
                getDescription());
    }
}
