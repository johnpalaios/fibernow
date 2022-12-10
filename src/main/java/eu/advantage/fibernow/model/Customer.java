package eu.advantage.fibernow.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
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
    @ElementCollection
    private Set<String> phoneNumber;
    private String email;
    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @ToString.Exclude
    private Set<Ticket> tickets;

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
