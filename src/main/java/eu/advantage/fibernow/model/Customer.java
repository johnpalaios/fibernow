package eu.advantage.fibernow.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "CUSTOMER")
public class Customer extends AbstractUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "tin")
    private String tin;
    @Column(name = "address")
    private String address;
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> phoneNumber = new HashSet<>();
    private String email;
    @OneToMany(
            mappedBy = "customer"
    )
    @ToString.Exclude
    private Set<Ticket> tickets = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getTin(), customer.getTin());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTin());
    }
}
