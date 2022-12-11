package eu.advantage.fibernow.dto;

import eu.advantage.fibernow.model.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
public class CustomerDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    @NotBlank
    private String tin;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    private String address;
    @NotEmpty
    private Set<String> phoneNumber = new HashSet<>();
    @NotBlank
    @Email
    private String email;
    @NotBlank
    @Size(min = 8)
    private String username;
    @NotBlank
    @Size(min = 8)
    private String password;
    private UserStatus userStatus;
    private Set<TicketDto> tickets = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerDto that = (CustomerDto) o;
        return Objects.equals(getTin(), that.getTin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTin());
    }
}
