package eu.advantage.fibernow.model;

import eu.advantage.fibernow.model.enums.Status;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "CUSTOMER")
@Table(name = "Customer",
        indexes = @Index(
                name = "idx_customer_tin",
                columnList = "tin",
                unique = true
        )
)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "cid")
    private Long id;
    private String tin;
    private String name;
    private String surname;
    private String address;
    @ElementCollection
    private Set<String> phoneNumber;
    @ElementCollection
    private Set<String> email;
    private String username;
    private String password;
    private Status status;
    @OneToMany(mappedBy = "customer")
    @ToString.Exclude
    private Set<Ticket> tickets;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Customer customer = (Customer) o;
        return id != null && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
